package com.funkycorgi.vulpecula.entry.biz.stub

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.models.ViewingDate
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class EntryCreateStubTest {
    private val processor = EntryProcessor()
    private val movieId = MovieId("movie-666")
    private val viewingDate = ViewingDate(LocalDate(2026, 1, 2))
    private val rating = 8
    private val comment = "create comment"

    @Test
    fun create() = runTest {
        val ctx = context(
            EntryStubs.SUCCESS,
            entryRequest = Entry(movieId = movieId, viewingDate = viewingDate, rating = rating, comment = comment),
        )

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertEquals(movieId, ctx.entryResponse.movieId)
        assertEquals(viewingDate, ctx.entryResponse.viewingDate)
        assertEquals(rating, ctx.entryResponse.rating)
        assertEquals(comment, ctx.entryResponse.comment)
    }

    @Test
    fun badMovieId() = runTest {
        val ctx = context(EntryStubs.BAD_MOVIE_ID)

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("movieId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = context(EntryStubs.DB_ERROR)

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = context(EntryStubs.BAD_ID)

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }

    private fun context(stub: EntryStubs, entryRequest: Entry = Entry()) = EntryContext(
        command = EntryCommand.CREATE,
        state = EntryState.NONE,
        workMode = EntryWorkMode.STUB,
        stubCase = stub,
        entryRequest = entryRequest,
    )
}
