package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorValidation
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.validateRatingInRange(title: String) = worker {
    this.title = title
    on { entryValidating.rating !in 1..10 }
    handle {
        fail(
            errorValidation(
                field = "rating",
                violationCode = "range",
                description = "field must be in range 1..10"
            )
        )
    }
}
