package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.repo.*
import kotlin.test.*

abstract class RepoEntryReadTest {
    abstract val repo: IRepoEntry
    protected open val readSuccess = initObjects[0]
    private val readIdNotFound = EntryId("entry-repo-read-not-found")

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readEntry(DbEntryIdRequest(readSuccess.id))
        assertIs<DbEntryResponseOk>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readEntry(DbEntryIdRequest(readIdNotFound))
        assertIs<DbEntryResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    companion object : BaseInitEntries("read") {
        override val initObjects: List<Entry> = listOf(createInitTestModel("success"))
    }
}
