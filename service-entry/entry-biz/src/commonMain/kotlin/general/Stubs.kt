package com.funkycorgi.vulpecula.entry.biz.general

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.chain

fun ICorChainDsl<EntryContext>.stubs(
    title: String,
    block: ICorChainDsl<EntryContext>.() -> Unit,
) = chain {
    block()
    this.title = title
    on { workMode == EntryWorkMode.STUB && state == EntryState.RUNNING }
}
