package com.funkycorgi.vulpecula.entry.app.ktor

import io.ktor.server.application.*
import com.funkycorgi.vulpecula.entry.api.jvm.models.*

suspend fun ApplicationCall.createEntry(appSettings: EntryAppSettings) =
    processEntry<EntryCreateRequest, EntryCreateResponse>(appSettings)

suspend fun ApplicationCall.readEntry(appSettings: EntryAppSettings) =
    processEntry<EntryReadRequest, EntryReadResponse>(appSettings)

suspend fun ApplicationCall.updateEntry(appSettings: EntryAppSettings) =
    processEntry<EntryUpdateRequest, EntryUpdateResponse>(appSettings)

suspend fun ApplicationCall.deleteEntry(appSettings: EntryAppSettings) =
    processEntry<EntryDeleteRequest, EntryDeleteResponse>(appSettings)

suspend fun ApplicationCall.searchEntry(appSettings: EntryAppSettings) =
    processEntry<EntrySearchRequest, EntrySearchResponse>(appSettings)
