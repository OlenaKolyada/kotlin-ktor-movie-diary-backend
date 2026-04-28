package com.funkycorgi.vulpecula.entry.common

import kotlinx.datetime.Instant
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs

data class EntryContext(
    var command: EntryCommand = EntryCommand.NONE,
    var state: EntryState = EntryState.NONE,
    val errors: MutableList<EntryError> = mutableListOf(),

    var corSettings: EntryCorSettings = EntryCorSettings(),
    var workMode: EntryWorkMode = EntryWorkMode.PROD,
    var stubCase: EntryStubs = EntryStubs.NONE,

    var requestId: EntryRequestId = EntryRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var entryRequest: Entry = Entry(),
    var entryFilterRequest: EntryFilter = EntryFilter(),

    var entryValidating: Entry = Entry(),
    var entryFilterValidating: EntryFilter = EntryFilter(),

    var entryValidated: Entry = Entry(),
    var entryFilterValidated: EntryFilter = EntryFilter(),

    var entryResponse: Entry = Entry(),
    var entriesResponse: MutableList<Entry> = mutableListOf(),
)
