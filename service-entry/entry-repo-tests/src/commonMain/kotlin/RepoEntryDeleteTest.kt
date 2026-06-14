package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.repo.*
import kotlin.test.*

abstract class RepoEntryDeleteTest {
    abstract val repo: IRepoEntry
    protected open val deleteSuccess = initObjects[0]
    private val deleteIdNotFound = EntryId("entry-repo-delete-not-found")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteEntry(DbEntryIdRequest(deleteSuccess))
        assertIs<DbEntryResponseOk>(result)
        assertEquals(deleteSuccess, result.data)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteEntry(DbEntryIdRequest(deleteIdNotFound))
        assertIs<DbEntryResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    companion object : BaseInitEntries("delete") {
        override val initObjects: List<Entry> = listOf(createInitTestModel("success"))
    }
}
