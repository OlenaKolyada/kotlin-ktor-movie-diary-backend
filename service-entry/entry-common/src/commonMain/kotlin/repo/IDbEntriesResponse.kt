package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryError

sealed interface IDbEntriesResponse : IDbResponse<List<Entry>>

data class DbEntriesResponseOk(
    override val data: List<Entry>,
) : IDbEntriesResponse {
    override val isSuccess: Boolean = true
    override val errors: List<EntryError> = emptyList()
}

data class DbEntriesResponseErr(
    override val errors: List<EntryError> = emptyList(),
) : IDbEntriesResponse {
    constructor(error: EntryError) : this(listOf(error))

    override val data: List<Entry>? = null
    override val isSuccess: Boolean = false
}
