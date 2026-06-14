package com.funkycorgi.vulpecula.entry.app.ktor.plugins

import io.ktor.server.application.*
import com.funkycorgi.vulpecula.entry.app.ktor.EntryAppSettings
import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.repo.inmemory.EntryRepoInMemory
import com.funkycorgi.vulpecula.entry.repo.stubs.EntryRepoStub

fun Application.initAppSettings(): EntryAppSettings {
    val corSettings = EntryCorSettings(
        repoTest = EntryRepoInMemory(),
        repoProd = EntryRepoInMemory(),
        repoStub = EntryRepoStub(),
    )
    return EntryAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = EntryProcessor(corSettings),
    )
}
