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

class EntryBizRepoCreateTest {
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = EntryRepositoryMock(
        invokeCreateEntry = {
            DbEntryResponseOk(it.entry.copy(id = EntryId(uuid), lock = EntryLock("lock-$uuid")))
        }
    )
    private val processor = EntryProcessor(EntryCorSettings(repoTest = repo))

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = EntryContext(
            command = EntryCommand.CREATE,
            state = EntryState.NONE,
            workMode = EntryWorkMode.TEST,
            entryRequest = Entry(
                movieId = MovieId("movie:123"),
                viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
                rating = 8,
                comment = "good movie",
            ),
        )

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertNotEquals(EntryId.NONE, ctx.entryResponse.id)
        assertEquals(MovieId("movie:123"), ctx.entryResponse.movieId)
        assertEquals(8, ctx.entryResponse.rating)
    }
}
