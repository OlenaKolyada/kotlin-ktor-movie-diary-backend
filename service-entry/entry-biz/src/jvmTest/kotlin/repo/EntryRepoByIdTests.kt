package repo

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.repo.DbEntryResponseOk
import com.funkycorgi.vulpecula.entry.common.repo.errorNotFound
import com.funkycorgi.vulpecula.entry.repo.tests.EntryRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initEntry = Entry(
    id = EntryId("entry:123"),
    movieId = MovieId("movie:123"),
    viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
    rating = 8,
    comment = "some comment",
)
private val repo = EntryRepositoryMock(
    invokeReadEntry = {
        if (it.id == initEntry.id)
            DbEntryResponseOk(initEntry)
        else errorNotFound(it.id)
    }
)
private val settings = EntryCorSettings(repoTest = repo)
private val processor = EntryProcessor(settings)

fun repoNotFoundTest(command: EntryCommand) = runTest {
    val ctx = EntryContext(
        command = command,
        state = EntryState.NONE,
        workMode = EntryWorkMode.TEST,
        entryRequest = Entry(
            id = EntryId("entry:not-found"),
            movieId = MovieId("movie:123"),
            viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
            rating = 8,
            comment = "some comment",
            lock = EntryLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(EntryState.FAILING, ctx.state)
    assertEquals(Entry(), ctx.entryResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
