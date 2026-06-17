package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.repo.DbEntryRequest
import com.funkycorgi.vulpecula.entry.common.repo.DbEntryResponseOk
import kotlin.test.Test
import kotlin.test.assertIs

class EntryRepositoryMockTest {
    private val repo = EntryRepositoryMock()

    @Test
    fun mockCreate() = runRepoTest {
        val result = repo.createEntry(DbEntryRequest(EntryRepositoryMock.DEFAULT_ENTRY_SUCCESS_EMPTY_MOCK.data))
        assertIs<DbEntryResponseOk>(result)
    }
}
