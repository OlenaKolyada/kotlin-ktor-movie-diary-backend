package com.funkycorgi.vulpecula.entry.biz.repo

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.prepareResult(title: String) = worker {
    this.title = title
    on { workMode != EntryWorkMode.STUB && entryRepo != com.funkycorgi.vulpecula.entry.common.repo.IRepoEntry.NONE }
    handle {
        entryResponse = entryRepoDone
        entriesResponse = entriesRepoDone
        state = when (val currentState = state) {
            EntryState.RUNNING -> EntryState.FINISHING
            else -> currentState
        }
    }
}
