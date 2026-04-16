package com.funkycorgi.vulpecula.entry.app.rabbit

import kotlinx.coroutines.runBlocking
import com.funkycorgi.vulpecula.entry.app.rabbit.config.EntryAppRabbitSettings
import com.funkycorgi.vulpecula.entry.app.rabbit.config.RabbitConfig
import com.funkycorgi.vulpecula.entry.app.rabbit.mappers.fromArgs

fun main(vararg args: String) = runBlocking {
    val appSettings = EntryAppRabbitSettings(
        rabbit = RabbitConfig.fromArgs(*args),
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}
