package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorValidation
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.validateMovieIdNotEmpty(title: String) = worker {
    this.title = title
    on { entryValidating.movieId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "movieId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
