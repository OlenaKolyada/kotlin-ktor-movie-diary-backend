package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryFilter
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.models.ViewingDate
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EntryValidationTest {
    private val processor = EntryProcessor()

    @Test
    fun createValidatesAndTrims() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.CREATE,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = validEntry(movieId = MovieId(" movie:123 "), comment = " good movie "),
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(EntryState.FAILING, ctx.state)
        assertEquals(MovieId("movie:123"), ctx.entryValidated.movieId)
        assertEquals("good movie", ctx.entryValidated.comment)
    }

    @Test
    fun createRejectsEmptyMovieId() = runTest {
        val ctx = createContext(validEntry(movieId = MovieId.NONE))

        processor.exec(ctx)

        assertEquals(EntryState.FAILING, ctx.state)
        assertEquals("movieId", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun createRejectsBadRating() = runTest {
        val ctx = createContext(validEntry(rating = 11))

        processor.exec(ctx)

        assertEquals(EntryState.FAILING, ctx.state)
        assertEquals("rating", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun createRejectsCommentWithoutLetters() = runTest {
        val ctx = createContext(validEntry(comment = "!@#$"))

        processor.exec(ctx)

        assertEquals(EntryState.FAILING, ctx.state)
        assertEquals("comment", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun readTrimsId() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.READ,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = Entry(id = EntryId(" entry:123 ")),
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertEquals(EntryId("entry:123"), ctx.entryValidated.id)
    }

    @Test
    fun readRejectsBadIdFormat() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.READ,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = Entry(id = EntryId("<bad>")),
        )

        processor.exec(ctx)

        assertEquals(EntryState.FAILING, ctx.state)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun updateRequiresLock() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.UPDATE,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = validEntry(id = EntryId("entry:123"), lock = EntryLock.NONE),
        )

        processor.exec(ctx)

        assertEquals(EntryState.FAILING, ctx.state)
        assertEquals("lock", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun deleteTrimsIdAndLock() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.DELETE,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = Entry(id = EntryId(" entry:123 "), lock = EntryLock(" lock-123 ")),
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertEquals(EntryId("entry:123"), ctx.entryValidated.id)
        assertEquals(EntryLock("lock-123"), ctx.entryValidated.lock)
    }

    @Test
    fun searchAllowsEmptySearchString() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.SEARCH,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryFilterRequest = EntryFilter(searchString = " "),
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertEquals("", ctx.entryFilterValidated.searchString)
    }

    @Test
    fun searchRejectsShortSearchString() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.SEARCH,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryFilterRequest = EntryFilter(searchString = "ab"),
        )

        processor.exec(ctx)

        assertEquals(EntryState.FAILING, ctx.state)
        assertEquals("searchString", ctx.errors.firstOrNull()?.field)
    }

    private fun createContext(entry: Entry) = EntryContext(
        command = EntryCommand.CREATE,
        state = EntryState.NONE,
        workMode = EntryWorkMode.TEST,
        entryRequest = entry,
    )

    private fun validEntry(
        id: EntryId = EntryId.NONE,
        movieId: MovieId = MovieId("movie:123"),
        viewingDate: ViewingDate = ViewingDate(LocalDate(2026, 1, 2)),
        rating: Int = 8,
        comment: String = "good movie",
        lock: EntryLock = EntryLock("lock-123"),
    ) = Entry(
        id = id,
        movieId = movieId,
        viewingDate = viewingDate,
        rating = rating,
        comment = comment,
        lock = lock,
    )
}
