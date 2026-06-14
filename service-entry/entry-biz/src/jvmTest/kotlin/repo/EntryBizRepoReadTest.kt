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

class EntryBizRepoReadTest {
    private val entry = Entry(
        id = EntryId("entry:123"),
        movieId = MovieId("movie:123"),
        viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
        rating = 8,
        comment = "good movie",
    )
    private val repo = EntryRepositoryMock(
        invokeReadEntry = { DbEntryResponseOk(entry) }
    )
    private val processor = EntryProcessor(EntryCorSettings(repoTest = repo))

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.READ,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = Entry(id = entry.id),
        )

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertEquals(entry, ctx.entryResponse)
    }
}
