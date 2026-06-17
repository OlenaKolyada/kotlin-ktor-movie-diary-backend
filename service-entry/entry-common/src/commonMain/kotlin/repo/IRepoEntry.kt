package com.funkycorgi.vulpecula.entry.common.repo

interface IRepoEntry {
    suspend fun createEntry(request: DbEntryRequest): IDbEntryResponse
    suspend fun readEntry(request: DbEntryIdRequest): IDbEntryResponse
    suspend fun updateEntry(request: DbEntryRequest): IDbEntryResponse
    suspend fun deleteEntry(request: DbEntryIdRequest): IDbEntryResponse
    suspend fun searchEntry(request: DbEntryFilterRequest): IDbEntriesResponse

    companion object {
        val NONE = object : IRepoEntry {
            override suspend fun createEntry(request: DbEntryRequest): IDbEntryResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readEntry(request: DbEntryIdRequest): IDbEntryResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateEntry(request: DbEntryRequest): IDbEntryResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteEntry(request: DbEntryIdRequest): IDbEntryResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchEntry(request: DbEntryFilterRequest): IDbEntriesResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
