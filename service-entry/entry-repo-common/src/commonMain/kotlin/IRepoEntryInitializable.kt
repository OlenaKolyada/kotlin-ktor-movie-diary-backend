package com.funkycorgi.vulpecula.entry.repo.common

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.repo.IRepoEntry

interface IRepoEntryInitializable : IRepoEntry {
    fun save(entries: Collection<Entry>): Collection<Entry>
}
