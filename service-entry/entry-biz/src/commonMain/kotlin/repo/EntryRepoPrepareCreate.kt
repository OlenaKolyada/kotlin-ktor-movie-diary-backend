package com.funkycorgi.vulpecula.entry.biz.repo

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    on { state == EntryState.RUNNING }
    handle {
        entryRepoPrepare = entryValidated.deepCopy()
    }
}
