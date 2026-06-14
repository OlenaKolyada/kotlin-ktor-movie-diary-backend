package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryError

sealed interface IDbEntryResponse : IDbResponse<Entry>

data class DbEntryResponseOk(
    override val data: Entry,
) : IDbEntryResponse {
    override val isSuccess: Boolean = true
    override val errors: List<EntryError> = emptyList()
}

data class DbEntryResponseErr(
    override val errors: List<EntryError> = emptyList(),
) : IDbEntryResponse {
    constructor(error: EntryError) : this(listOf(error))

    override val data: Entry? = null
    override val isSuccess: Boolean = false
}

data class DbEntryResponseErrWithData(
    override val data: Entry,
    override val errors: List<EntryError> = emptyList(),
) : IDbEntryResponse {
    constructor(entry: Entry, error: EntryError) : this(entry, listOf(error))

    override val isSuccess: Boolean = false
}
