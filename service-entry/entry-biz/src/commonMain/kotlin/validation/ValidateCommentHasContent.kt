package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorValidation
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.validateCommentHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { entryValidating.comment.isNotEmpty() && !entryValidating.comment.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "comment",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
