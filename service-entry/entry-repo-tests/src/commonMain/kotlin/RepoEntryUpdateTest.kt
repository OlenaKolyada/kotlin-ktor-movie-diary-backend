package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.repo.*
import kotlin.test.*

abstract class RepoEntryUpdateTest {
    abstract val repo: IRepoEntry
    protected open val updateSuccess = initObjects[0]
    protected open val updateConc = initObjects[1]
    private val updateIdNotFound = EntryId("entry-repo-update-not-found")
    protected val lockBad = EntryLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = EntryLock("20000000-0000-0000-0000-000000000002")

    private val requestUpdateSuccess by lazy {
        updateSuccess.copy(
            rating = 10,
            comment = "updated object comment",
            lock = initObjects.first().lock,
        )
    }

    private val requestUpdateNotFound by lazy {
        requestUpdateSuccess.copy(id = updateIdNotFound)
    }

    private val requestUpdateConc by lazy {
        updateConc.copy(
            rating = 10,
            comment = "updated object comment",
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateEntry(DbEntryRequest(requestUpdateSuccess))
        assertIs<DbEntryResponseOk>(result)
        assertEquals(requestUpdateSuccess.id, result.data.id)
        assertEquals(requestUpdateSuccess.rating, result.data.rating)
        assertEquals(requestUpdateSuccess.comment, result.data.comment)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateEntry(DbEntryRequest(requestUpdateNotFound))
        assertIs<DbEntryResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateEntry(DbEntryRequest(requestUpdateConc))
        assertIs<DbEntryResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitEntries("update") {
        override val initObjects: List<Entry> = listOf(
            createInitTestModel("success"),
            createInitTestModel("conc"),
        )
    }
}
