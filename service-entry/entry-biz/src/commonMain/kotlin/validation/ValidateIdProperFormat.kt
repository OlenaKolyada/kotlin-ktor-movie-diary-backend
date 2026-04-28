package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorValidation
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { entryValidating.id != EntryId.NONE && !entryValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = entryValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters, digits, #, :, or -"
            )
        )
    }
}
