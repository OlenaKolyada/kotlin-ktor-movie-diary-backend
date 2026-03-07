package com.funkycorgi.vulpecula.entry.biz

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.stubs.EntryStub

@Suppress("unused", "RedundantSuspendModifier")
class EntryProcessor(val corSettings: EntryCorSettings = EntryCorSettings()) {

    suspend fun exec(ctx: EntryContext) {
        ctx.entryResponse = EntryStub.get()
        ctx.entriesResponse = EntryStub.prepareSearchList("entry search").toMutableList()
        ctx.state = EntryState.RUNNING
    }
}
