package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.finishEntryValidation(title: String) = worker {
    this.title = title
    on { state == EntryState.RUNNING }
    handle {
        entryValidated = entryValidating
    }
}

fun ICorChainDsl<EntryContext>.finishEntryFilterValidation(title: String) = worker {
    this.title = title
    on { state == EntryState.RUNNING }
    handle {
        entryFilterValidated = entryFilterValidating
    }
}
