package com.funkycorgi.vulpecula.entry.biz.repo

import com.funkycorgi.vulpecula.entry.biz.exceptions.EntryDbNotConfiguredException
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorSystem
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.repo.IRepoEntry
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.initRepo(title: String) = worker {
    this.title = title
    handle {
        entryRepo = when (workMode) {
            EntryWorkMode.TEST -> corSettings.repoTest
            EntryWorkMode.STUB -> corSettings.repoStub
            EntryWorkMode.PROD -> corSettings.repoProd
        }
        if (workMode == EntryWorkMode.PROD && entryRepo == IRepoEntry.NONE) {
            fail(
                errorSystem(
                    violationCode = "dbNotConfigured",
                    e = EntryDbNotConfiguredException(workMode),
                )
            )
        }
    }
}
