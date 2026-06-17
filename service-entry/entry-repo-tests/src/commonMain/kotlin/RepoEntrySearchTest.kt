package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.repo.DbEntriesResponseOk
import com.funkycorgi.vulpecula.entry.common.repo.DbEntryFilterRequest
import com.funkycorgi.vulpecula.entry.common.repo.IRepoEntry
import kotlin.test.*

abstract class RepoEntrySearchTest {
    abstract val repo: IRepoEntry

    @Test
    fun searchByMovieId() = runRepoTest {
        val result = repo.searchEntry(DbEntryFilterRequest(movieId = initObjects[0].movieId))
        assertIs<DbEntriesResponseOk>(result)
        assertEquals(1, result.data.size)
        assertEquals(initObjects[0].id, result.data.first().id)
    }

    @Test
    fun searchByComment() = runRepoTest {
        val result = repo.searchEntry(DbEntryFilterRequest(searchString = "second"))
        assertIs<DbEntriesResponseOk>(result)
        assertEquals(1, result.data.size)
        assertEquals(initObjects[1].id, result.data.first().id)
    }

    companion object : BaseInitEntries("search") {
        override val initObjects: List<Entry> = listOf(
            createInitTestModel("first").copy(movieId = MovieId("movie-search-first")),
            createInitTestModel("second").copy(movieId = MovieId("movie-search-second"), comment = "second comment"),
        )
    }
}
