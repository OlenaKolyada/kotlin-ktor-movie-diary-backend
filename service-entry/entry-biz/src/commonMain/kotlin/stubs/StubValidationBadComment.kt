package com.funkycorgi.vulpecula.entry.biz.stubs

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.stubValidationBadComment(title: String) = worker {
    this.title = title
    on { stubCase == EntryStubs.BAD_COMMENT && state == EntryState.RUNNING }
    handle {
        fail(
            EntryError(
                group = "validation",
                code = "validation-comment",
                field = "comment",
                message = "Wrong comment field"
            )
        )
    }
}
