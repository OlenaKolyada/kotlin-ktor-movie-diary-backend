package com.funkycorgi.vulpecula.entry.biz.stubs

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == EntryState.RUNNING }
    handle {
        fail(
            EntryError(
                group = "validation",
                code = "validation-stub",
                field = "stub",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
