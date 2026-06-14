package repo

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.repo.DbEntryResponseOk
import com.funkycorgi.vulpecula.entry.repo.tests.EntryRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EntryBizRepoUpdateTest {
    private val command = EntryCommand.UPDATE

    private val entryFromRepo = Entry(
        id = EntryId("entry:123"),
        movieId = MovieId("movie:123"),
        viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
        rating = 8,
        comment = "original comment",
        lock = EntryLock("123-234-abc-ABC"),
    )

    private val repo = EntryRepositoryMock(
        invokeReadEntry = { DbEntryResponseOk(entryFromRepo) },
        invokeUpdateEntry = {
            DbEntryResponseOk(
                it.entry.copy(
                    movieId = MovieId("movie:updated"),
                    rating = 10,
                    comment = "updated comment",
                    lock = EntryLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val processor = EntryProcessor(EntryCorSettings(repoTest = repo))

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val entryToUpdate = Entry(
            id = EntryId("entry:123"),
            movieId = MovieId("movie:updated"),
            viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
            rating = 10,
            comment = "updated comment",
            lock = EntryLock("123-234-abc-ABC"),
        )
        val ctx = EntryContext(
            command = command,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = entryToUpdate,
        )

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertEquals(entryToUpdate.id, ctx.entryResponse.id)
        assertEquals(entryToUpdate.rating, ctx.entryResponse.rating)
        assertEquals(entryToUpdate.comment, ctx.entryResponse.comment)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
