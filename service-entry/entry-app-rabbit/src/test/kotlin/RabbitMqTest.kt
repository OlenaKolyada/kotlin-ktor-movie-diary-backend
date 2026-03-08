package com.funkycorgi.vulpecula.entry.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import com.funkycorgi.vulpecula.entry.api.jvm.entryApiJvmSerializer
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryCreateRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryCreateResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryDebug
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryRequestDebugMode
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryRequestDebugStubs
import com.funkycorgi.vulpecula.entry.app.rabbit.config.EntryAppRabbitSettings
import com.funkycorgi.vulpecula.entry.app.rabbit.config.RabbitConfig
import com.funkycorgi.vulpecula.entry.app.rabbit.config.RabbitExchangeConfiguration
import com.funkycorgi.vulpecula.entry.stubs.EntryStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
        const val RMQ_PORT = 5672

        private val container = RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(RMQ_PORT)
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = EntryAppRabbitSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
        controllersConfig = RabbitExchangeConfiguration(
            keyIn = "in",
            keyOut = "out",
            exchange = exchange,
            queue = "test-queue",
            consumerTag = "consumer-test",
            exchangeType = exchangeType
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun entryCreateTest() {
        val (keyOut, keyIn) = with(appSettings.controllersConfig) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, entryApiJvmSerializer.writeValueAsBytes(entryCreateRequest))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = entryApiJvmSerializer.readValue(responseJson, EntryCreateResponse::class.java)
                val expected = EntryStub.get()

                assertEquals(expected.movieId.asString(), response.entry?.movieId)
                assertEquals(expected.rating, response.entry?.rating)
            }
        }
    }

    private val entryCreateRequest = with(EntryStub.get()) {
        EntryCreateRequest(
            entry = EntryCreateObject(
                movieId = movieId.asString(),
                rating = rating,
            ),
            requestType = "create",
            debug = EntryDebug(
                mode = EntryRequestDebugMode.STUB,
                stub = EntryRequestDebugStubs.SUCCESS
            )
        )
    }
}
