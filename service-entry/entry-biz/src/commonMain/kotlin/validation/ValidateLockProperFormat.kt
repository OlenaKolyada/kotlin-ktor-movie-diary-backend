package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorValidation
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.validateLockProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { entryValidating.lock != EntryLock.NONE && !entryValidating.lock.asString().matches(regExp) }
    handle {
        val encodedLock = entryValidating.lock.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedLock must contain only letters, digits, or -"
            )
        )
    }
}
