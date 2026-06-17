package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.repo.DbEntryRequest
import com.funkycorgi.vulpecula.entry.common.repo.DbEntryResponseOk
import com.funkycorgi.vulpecula.entry.repo.common.IRepoEntryInitializable
import kotlinx.datetime.LocalDate
import kotlin.test.*

abstract class RepoEntryCreateTest {
    abstract val repo: IRepoEntryInitializable
    protected open val uuidNew = EntryId("10000000-0000-0000-0000-000000000001")

    private val createObj = Entry(
        userId = UserId("user-create"),
        movieId = MovieId("movie-create"),
        viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
        rating = 9,
        comment = "create object comment",
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createEntry(DbEntryRequest(createObj))
        assertIs<DbEntryResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(createObj.movieId, result.data.movieId)
        assertEquals(createObj.viewingDate, result.data.viewingDate)
        assertEquals(createObj.rating, result.data.rating)
        assertEquals(createObj.comment, result.data.comment)
        assertNotEquals(EntryId.NONE, result.data.id)
    }

    companion object : BaseInitEntries("create") {
        override val initObjects: List<Entry> = emptyList()
    }
}
