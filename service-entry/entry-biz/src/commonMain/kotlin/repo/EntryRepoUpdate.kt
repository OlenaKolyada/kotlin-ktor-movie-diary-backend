package com.funkycorgi.vulpecula.entry.biz.repo

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.repo.*
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == EntryState.RUNNING && entryRepo != IRepoEntry.NONE }
    handle {
        when (val result = entryRepo.updateEntry(DbEntryRequest(entryRepoPrepare))) {
            is DbEntryResponseOk -> entryRepoDone = result.data
            is DbEntryResponseErr -> fail(result.errors)
            is DbEntryResponseErrWithData -> {
                fail(result.errors)
                entryRepoDone = result.data
            }
        }
    }
}
