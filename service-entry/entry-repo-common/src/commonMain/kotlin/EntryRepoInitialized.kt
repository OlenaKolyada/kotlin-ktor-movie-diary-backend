package com.funkycorgi.vulpecula.entry.repo.common

import com.funkycorgi.vulpecula.entry.common.models.Entry

class EntryRepoInitialized(
    private val repo: IRepoEntryInitializable,
    initObjects: Collection<Entry> = emptyList(),
) : IRepoEntryInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Entry> = save(initObjects).toList()
}
