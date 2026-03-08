package com.funkycorgi.vulpecula.entry.app.rabbit.config

import com.funkycorgi.vulpecula.entry.app.common.IEntryAppSettings

interface IEntryAppRabbitSettings : IEntryAppSettings {
    val rabbit: RabbitConfig
    val controllersConfig: RabbitExchangeConfiguration
}
