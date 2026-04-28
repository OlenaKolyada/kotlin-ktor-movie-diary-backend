package com.funkycorgi.vulpecula.entry.biz.general

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.chain

fun ICorChainDsl<EntryContext>.operation(
    title: String,
    command: EntryCommand,
    block: ICorChainDsl<EntryContext>.() -> Unit,
) = chain {
    block()
    this.title = title
    on { this.command == command && state == EntryState.RUNNING }
}
