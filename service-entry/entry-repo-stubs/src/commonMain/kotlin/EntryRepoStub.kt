package com.funkycorgi.vulpecula.entry.repo.stubs

import com.funkycorgi.vulpecula.entry.common.repo.*
import com.funkycorgi.vulpecula.entry.stubs.EntryStub

class EntryRepoStub : IRepoEntry {
    override suspend fun createEntry(request: DbEntryRequest): IDbEntryResponse =
        DbEntryResponseOk(EntryStub.get())

    override suspend fun readEntry(request: DbEntryIdRequest): IDbEntryResponse =
        DbEntryResponseOk(EntryStub.get())

    override suspend fun updateEntry(request: DbEntryRequest): IDbEntryResponse =
        DbEntryResponseOk(EntryStub.get())

    override suspend fun deleteEntry(request: DbEntryIdRequest): IDbEntryResponse =
        DbEntryResponseOk(EntryStub.get())

    override suspend fun searchEntry(request: DbEntryFilterRequest): IDbEntriesResponse =
        DbEntriesResponseOk(EntryStub.prepareSearchList(request.searchString))
}
