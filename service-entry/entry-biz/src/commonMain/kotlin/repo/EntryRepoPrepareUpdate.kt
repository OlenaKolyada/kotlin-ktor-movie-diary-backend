package com.funkycorgi.vulpecula.entry.biz.repo

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    on { state == EntryState.RUNNING }
    handle {
        entryRepoPrepare = entryRepoRead.deepCopy().apply {
            movieId = entryValidated.movieId
            viewingDate = entryValidated.viewingDate
            rating = entryValidated.rating
            comment = entryValidated.comment
            lock = entryValidated.lock
        }
    }
}
