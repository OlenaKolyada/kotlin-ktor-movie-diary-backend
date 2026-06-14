package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.repo.*

class EntryRepositoryMock(
    private val invokeCreateEntry: (DbEntryRequest) -> IDbEntryResponse = { DEFAULT_ENTRY_SUCCESS_EMPTY_MOCK },
    private val invokeReadEntry: (DbEntryIdRequest) -> IDbEntryResponse = { DEFAULT_ENTRY_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateEntry: (DbEntryRequest) -> IDbEntryResponse = { DEFAULT_ENTRY_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteEntry: (DbEntryIdRequest) -> IDbEntryResponse = { DEFAULT_ENTRY_SUCCESS_EMPTY_MOCK },
    private val invokeSearchEntry: (DbEntryFilterRequest) -> IDbEntriesResponse = { DEFAULT_ENTRIES_SUCCESS_EMPTY_MOCK },
) : IRepoEntry {
    override suspend fun createEntry(request: DbEntryRequest): IDbEntryResponse = invokeCreateEntry(request)
    override suspend fun readEntry(request: DbEntryIdRequest): IDbEntryResponse = invokeReadEntry(request)
    override suspend fun updateEntry(request: DbEntryRequest): IDbEntryResponse = invokeUpdateEntry(request)
    override suspend fun deleteEntry(request: DbEntryIdRequest): IDbEntryResponse = invokeDeleteEntry(request)
    override suspend fun searchEntry(request: DbEntryFilterRequest): IDbEntriesResponse = invokeSearchEntry(request)

    companion object {
        val DEFAULT_ENTRY_SUCCESS_EMPTY_MOCK = DbEntryResponseOk(Entry())
        val DEFAULT_ENTRIES_SUCCESS_EMPTY_MOCK = DbEntriesResponseOk(emptyList())
    }
}
