package com.funkycorgi.vulpecula.entry.biz.stubs

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.models.ViewingDate
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker
import com.funkycorgi.vulpecula.entry.stubs.EntryStub

fun ICorChainDsl<EntryContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == EntryStubs.SUCCESS && state == EntryState.RUNNING }
    handle {
        state = EntryState.FINISHING
        entryResponse = EntryStub.prepareResult {
            entryRequest.movieId.takeIf { it != MovieId.NONE }?.also { movieId = it }
            entryRequest.viewingDate.takeIf { it != ViewingDate.NONE }?.also { viewingDate = it }
            entryRequest.rating.takeIf { it > 0 }?.also { rating = it }
            entryRequest.comment.takeIf { it.isNotBlank() }?.also { comment = it }
        }
    }
}
