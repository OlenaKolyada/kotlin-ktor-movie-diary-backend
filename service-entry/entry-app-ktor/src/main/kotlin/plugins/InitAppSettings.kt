package com.funkycorgi.vulpecula.entry.app.ktor.plugins

import io.ktor.server.application.*
import com.funkycorgi.vulpecula.entry.app.ktor.EntryAppSettings
import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings

fun Application.initAppSettings(): EntryAppSettings {
    val corSettings = EntryCorSettings()
    return EntryAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = EntryProcessor(corSettings),
    )
}
