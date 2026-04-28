package com.funkycorgi.vulpecula.entry.biz

import com.funkycorgi.vulpecula.entry.cor.rootChain
import com.funkycorgi.vulpecula.entry.cor.worker
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.stubs.EntryStub

@Suppress("unused", "RedundantSuspendModifier")
class EntryProcessor(val corSettings: EntryCorSettings = EntryCorSettings()) {
    private val chain = rootChain<EntryContext> {
        title = "Entry processing chain"

        worker("Prepare stub response") {
            entryResponse = EntryStub.get()
            entriesResponse = EntryStub.prepareSearchList("entry search").toMutableList()
        }

        worker("Set successful processing state") {
            state = EntryState.RUNNING
        }
    }.build()

    suspend fun exec(ctx: EntryContext) {
        chain.exec(ctx)
    }
}
