package com.funkycorgi.vulpecula.entry.app.rabbit.controllers

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.funkycorgi.vulpecula.entry.app.rabbit.config.RabbitConfig
import com.funkycorgi.vulpecula.entry.app.rabbit.config.RabbitExchangeConfiguration
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

/**
 * Абстрактный класс для контроллеров-консьюмеров RabbitMQ
 * @property rabbitConfig - настройки подключения
 * @property exchangeConfig - настройки Rabbit exchange
 */
abstract class RabbitProcessorBase @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val rabbitConfig: RabbitConfig,
    override val exchangeConfig: RabbitExchangeConfiguration,
    private val dispatcher: CoroutineContext = Dispatchers.IO.limitedParallelism(1) + Job(),
) : IRabbitController {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var conn: Connection? = null
    private var chan: Channel? = null

    override suspend fun process() = withContext(dispatcher) {
        ConnectionFactory().apply {
            host = rabbitConfig.host
            port = rabbitConfig.port
            username = rabbitConfig.user
            password = rabbitConfig.password
        }.newConnection().use { connection ->
            logger.debug("Creating new connection [${exchangeConfig.consumerTag}]")
            conn = connection
            connection.createChannel().use { channel ->
                logger.debug("Creating new channel [${exchangeConfig.consumerTag}]")
                chan = channel
                val deliveryCallback = channel.getDeliveryCallback()
                val cancelCallback = getCancelCallback()
                channel.describeAndListen(deliveryCallback, cancelCallback)
            }
        }
    }

    /**
     * Обработка поступившего сообщения в deliverCallback
     */
    protected abstract suspend fun Channel.processMessage(message: Delivery)

    /**
     * Обработка ошибок
     */
    protected abstract fun Channel.onError(e: Throwable, delivery: Delivery)

    /**
     * Callback, который вызывается при доставке сообщения консьюмеру
     */
    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, delivery: Delivery ->
        runBlocking {
            kotlin.runCatching {
                val deliveryTag: Long = delivery.envelope.deliveryTag
                processMessage(delivery)
                // Ручное подтверждение завершения обработки сообщения
                this@getDeliveryCallback.basicAck(deliveryTag, false)
            }.onFailure {
                onError(it, delivery)
            }
        }
    }

    /**
     * Callback, вызываемый при отмене консьюмера
     */
    private fun getCancelCallback() = CancelCallback {
        logger.debug("[$it] was cancelled")
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback,
        cancelCallback: CancelCallback
    ) {
        withContext(Dispatchers.IO) {
            exchangeDeclare(exchangeConfig.exchange, exchangeConfig.exchangeType)
            queueDeclare(exchangeConfig.queue, false, false, false, null)
            queueBind(exchangeConfig.queue, exchangeConfig.exchange, exchangeConfig.keyIn)
            basicConsume(
                exchangeConfig.queue,
                false, // autoAck отключен. Подтверждение в deliverCallback
                exchangeConfig.consumerTag,
                deliverCallback,
                cancelCallback
            )

            // Блокируем текущий поток для обработки фоновых процессов
            while (isOpen) {
                runCatching {
                    delay(100)
                }.recover { }
            }
            logger.debug("Channel for [${exchangeConfig.consumerTag}] was closed.")
        }
    }

    override fun close() {
        logger.debug("CLOSE is requested [${exchangeConfig.consumerTag}]")
        chan?.takeIf { it.isOpen }?.run {
            basicCancel(exchangeConfig.consumerTag)
            close()
            logger.debug("Close channel [${exchangeConfig.consumerTag}]")
        }
        conn?.takeIf { it.isOpen }?.run {
            close()
            logger.debug("Close Rabbit connection [${exchangeConfig.consumerTag}]")
        }
    }
}
