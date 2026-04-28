package com.funkycorgi.vulpecula.entry.biz.stubs

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker
import com.funkycorgi.vulpecula.entry.stubs.EntryStub

fun ICorChainDsl<EntryContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == EntryStubs.SUCCESS && state == EntryState.RUNNING }
    handle {
        state = EntryState.FINISHING
        entryResponse = EntryStub.prepareResult {
            entryRequest.id.takeIf { it != EntryId.NONE }?.also { id = it }
        }
    }
}
