package infrastructure

import co.touchlab.kermit.Logger
import io.ktor.http.*
import org.slf4j.LoggerFactory
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

private val log = Logger

/**
 * services - список приложений в docker-compose. Первое приложение - "главное", его url возвращается как inputUrl
 * (например ваш сервис при работе по rest или брокер сообщений при работе с брокером)
 * dockerComposeNames - имена docker-compose файлов (относительно ok-marketplace-acceptance/docker-compose)
 */
abstract class AbstractTestEnvironment(
    private val services: List<ServiceInfo>,
    private val composeFiles: List<String>,
) : TestEnvironment {
    constructor(
        service: String,
        port: Int,
        vararg dockerComposeName: String
    ): this(
        listOf(ServiceInfo(service, port)),
        dockerComposeName.toList()
    )
    private val LOGGER = LoggerFactory.getLogger(ComposeContainer::class.java)
    private val logConsumer = Slf4jLogConsumer(LOGGER)
    private fun getComposeFiles(): List<File> = composeFiles.map {
        val file = File("docker-compose/$it")
        if (!file.exists()) throw IllegalArgumentException("file $it not found!")
        file
    }

    private val compose by lazy {
        ComposeContainer(getComposeFiles()).apply {
            services.forEach { (service, port) ->
                withExposedService(service, port)
                withLogConsumer(service, logConsumer)
                withStartupTimeout(Duration.ofSeconds(600))
                waitingFor(service, Wait.forHealthcheck())
            }
        }
    }

    override fun start() {
        runCatching { compose.start() }.onFailure {
            log.e { "Failed to start $composeFiles" }
            throw it
        }

        log.w("\n=========== $composeFiles started =========== \n")
        services.forEachIndexed { index, _ ->
            log.i { "Started docker-compose with App at: ${getUrl(index)}" }
        }
    }

    override fun stop() {
        compose.close()
        log.w("\n=========== $composeFiles complete =========== \n")
    }

    override fun clearDb() {
        log.w("===== clearDb =====")
        // TODO сделать очистку БД, когда до этого дойдет
    }

    override val inputUrl: URLBuilder
        get() = getUrl(0)

    fun getUrl(index: Int) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = services[index].let { compose.getServiceHost(it.service, it.port) },
        port = services[index].let { compose.getServicePort(it.service, it.port) },
    )
    data class ServiceInfo(
        val service: String,
        val port: Int,
    )

}
