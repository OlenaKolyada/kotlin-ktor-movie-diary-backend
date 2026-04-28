package com.funkycorgi.vulpecula.entry.biz.stubs

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == EntryStubs.DB_ERROR && state == EntryState.RUNNING }
    handle {
        fail(
            EntryError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
