package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorValidation
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.validateMovieIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { entryValidating.movieId != MovieId.NONE && !entryValidating.movieId.asString().matches(regExp) }
    handle {
        val encodedMovieId = entryValidating.movieId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "movieId",
                violationCode = "badFormat",
                description = "value $encodedMovieId must contain only letters, digits, #, :, or -"
            )
        )
    }
}
