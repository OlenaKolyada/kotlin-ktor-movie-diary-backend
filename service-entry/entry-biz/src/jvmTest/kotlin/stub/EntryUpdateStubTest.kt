package com.funkycorgi.vulpecula.entry.biz.stub

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EntryUpdateStubTest {
    private val processor = EntryProcessor()
    private val id = EntryId("entry-777")
    private val movieId = MovieId("movie-777")
    private val rating = 7
    private val comment = "update comment"

    @Test
    fun update() = runTest {
        val ctx = context(
            EntryStubs.SUCCESS,
            Entry(id = id, movieId = movieId, rating = rating, comment = comment),
        )

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertEquals(id, ctx.entryResponse.id)
        assertEquals(movieId, ctx.entryResponse.movieId)
        assertEquals(rating, ctx.entryResponse.rating)
        assertEquals(comment, ctx.entryResponse.comment)
    }

    @Test
    fun badRating() = runTest {
        val ctx = context(EntryStubs.BAD_RATING)

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("rating", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = context(EntryStubs.BAD_SEARCH_STRING, Entry(id = id))

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }

    private fun context(stub: EntryStubs, entryRequest: Entry = Entry()) = EntryContext(
        command = EntryCommand.UPDATE,
        state = EntryState.NONE,
        workMode = EntryWorkMode.STUB,
        stubCase = stub,
        entryRequest = entryRequest,
    )
}
