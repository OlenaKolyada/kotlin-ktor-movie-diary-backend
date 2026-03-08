package com.funkycorgi.vulpecula.entry.app.rabbit.config

import com.funkycorgi.vulpecula.entry.app.common.IEntryAppSettings
import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings

data class EntryAppRabbitSettings(
    override val corSettings: EntryCorSettings = EntryCorSettings(),
    override val processor: EntryProcessor = EntryProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfig: RabbitExchangeConfiguration = RabbitExchangeConfiguration(
        keyIn = "entry-in",
        keyOut = "entry-out",
        exchange = "entry-exchange",
        queue = "entry-queue",
        consumerTag = "entry-consumer",
        exchangeType = "direct"
    ),
) : IEntryAppSettings, IEntryAppRabbitSettings
