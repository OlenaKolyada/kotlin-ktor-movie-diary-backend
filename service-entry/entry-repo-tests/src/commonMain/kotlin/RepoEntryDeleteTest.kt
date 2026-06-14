package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.repo.*
import kotlin.test.*

abstract class RepoEntryDeleteTest {
    abstract val repo: IRepoEntry
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConc = initObjects[1]
    private val deleteIdNotFound = EntryId("entry-repo-delete-not-found")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteEntry(DbEntryIdRequest(deleteSuccess))
        assertIs<DbEntryResponseOk>(result)
        assertEquals(deleteSuccess, result.data)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteEntry(DbEntryIdRequest(deleteIdNotFound, lock = lockOld))
        assertIs<DbEntryResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrencyError() = runRepoTest {
        val result = repo.deleteEntry(DbEntryIdRequest(deleteConc.id, lock = lockBad))
        assertIs<DbEntryResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitEntries("delete") {
        override val initObjects: List<Entry> = listOf(
            createInitTestModel("success"),
            createInitTestModel("conc"),
        )
    }
}
