package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.chain

fun ICorChainDsl<EntryContext>.validation(block: ICorChainDsl<EntryContext>.() -> Unit) = chain {
    block()
    title = "Validation"
    on { state == EntryState.RUNNING }
}
