package com.funkycorgi.vulpecula.entry.common

import com.funkycorgi.vulpecula.entry.common.repo.IRepoEntry

data class EntryCorSettings(
    val repoStub: IRepoEntry = IRepoEntry.NONE,
    val repoTest: IRepoEntry = IRepoEntry.NONE,
    val repoProd: IRepoEntry = IRepoEntry.NONE,
) {
    companion object {
        val NONE = EntryCorSettings()
    }
}
