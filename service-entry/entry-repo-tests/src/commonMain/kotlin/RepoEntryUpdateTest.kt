package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.repo.*
import kotlin.test.*

abstract class RepoEntryUpdateTest {
    abstract val repo: IRepoEntry
    protected open val updateSuccess = initObjects[0]
    private val updateIdNotFound = EntryId("entry-repo-update-not-found")

    private val requestUpdateSuccess by lazy {
        updateSuccess.copy(
            rating = 10,
            comment = "updated object comment",
        )
    }

    private val requestUpdateNotFound by lazy {
        requestUpdateSuccess.copy(id = updateIdNotFound)
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateEntry(DbEntryRequest(requestUpdateSuccess))
        assertIs<DbEntryResponseOk>(result)
        assertEquals(requestUpdateSuccess.id, result.data.id)
        assertEquals(requestUpdateSuccess.rating, result.data.rating)
        assertEquals(requestUpdateSuccess.comment, result.data.comment)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateEntry(DbEntryRequest(requestUpdateNotFound))
        assertIs<DbEntryResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    companion object : BaseInitEntries("update") {
        override val initObjects: List<Entry> = listOf(createInitTestModel("success"))
    }
}
