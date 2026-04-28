package com.funkycorgi.vulpecula.entry.biz.general

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == EntryState.NONE }
    handle { state = EntryState.RUNNING }
}
