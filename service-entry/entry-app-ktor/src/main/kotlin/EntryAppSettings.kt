package com.funkycorgi.vulpecula.entry.app.ktor

import com.funkycorgi.vulpecula.entry.app.common.IEntryAppSettings
import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings

data class EntryAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: EntryCorSettings = EntryCorSettings(),
    override val processor: EntryProcessor = EntryProcessor(corSettings),
) : IEntryAppSettings
