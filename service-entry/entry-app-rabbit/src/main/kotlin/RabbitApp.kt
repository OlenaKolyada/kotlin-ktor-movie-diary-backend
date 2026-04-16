package com.funkycorgi.vulpecula.entry.app.rabbit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import com.funkycorgi.vulpecula.entry.app.rabbit.config.EntryAppRabbitSettings
import com.funkycorgi.vulpecula.entry.app.rabbit.controllers.IRabbitController
import com.funkycorgi.vulpecula.entry.app.rabbit.controllers.RabbitDirectController
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

// Класс запускает все контроллеры
@OptIn(ExperimentalCoroutinesApi::class)
class RabbitApp(
    appSettings: EntryAppRabbitSettings,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : AutoCloseable {
    private val logger = LoggerFactory.getLogger(RabbitApp::class.java)
    private val controllers: List<IRabbitController> = listOf(
        RabbitDirectController(appSettings),
    )
    private val runFlag = AtomicBoolean(true)

    fun start() {
        runFlag.set(true)
        controllers.forEach {
            scope.launch(
                Dispatchers.IO.limitedParallelism(1) + CoroutineName("thread-${it.exchangeConfig.consumerTag}")
            ) {
                while (runFlag.get()) {
                    try {
                        logger.info("Process...${it.exchangeConfig.consumerTag}")
                        it.process()
                    } catch (e: RuntimeException) {
                        logger.error("Обработка завалена, возможно из-за потери соединения с RabbitMQ. Рестартуем")
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun close() {
        runFlag.set(false)
        controllers.forEach { it.close() }
        scope.cancel()
    }
}
