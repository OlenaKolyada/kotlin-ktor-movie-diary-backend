package repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.funkycorgi.vulpecula.entry.api.jvm.models.*
import com.funkycorgi.vulpecula.entry.app.ktor.EntryAppSettings
import com.funkycorgi.vulpecula.entry.app.ktor.module
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.repo.common.EntryRepoInitialized
import com.funkycorgi.vulpecula.entry.repo.inmemory.EntryRepoInMemory
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EntryRepoApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val initialEntry = Entry(
        id = EntryId(uuidOld),
        userId = UserId("user:repo"),
        movieId = MovieId("movie:repo"),
        viewingDate = ViewingDate(LocalDate(2026, 1, 2)),
        rating = 8,
        comment = "repo test comment",
        lock = EntryLock("lock-$uuidOld"),
    )

    @Test
    fun create() = entryRepoTestApplication(
        func = "create",
        appSettings = appSettings(
            repo = EntryRepoInitialized(EntryRepoInMemory(randomUuid = { uuidNew }))
        ),
        request = EntryCreateRequest(
            entry = EntryCreateObject(
                movieId = "movie:create",
                viewingDate = "2026-01-02",
                rating = 9,
                comment = "created repo entry",
            ),
            debug = EntryDebug(mode = EntryRequestDebugMode.TEST),
        ),
    ) { response ->
        val responseObj = response.body<EntryCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.entry?.id)
        assertEquals("movie:create", responseObj.entry?.movieId)
        assertEquals(9, responseObj.entry?.rating)
    }

    @Test
    fun read() = entryRepoTestApplication(
        func = "read",
        appSettings = appSettings(
            repo = EntryRepoInitialized(
                EntryRepoInMemory(),
                initObjects = listOf(initialEntry),
            )
        ),
        request = EntryReadRequest(
            entry = EntryReadObject(uuidOld),
            debug = EntryDebug(mode = EntryRequestDebugMode.TEST),
        ),
    ) { response ->
        val responseObj = response.body<EntryReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.entry?.id)
        assertEquals("movie:repo", responseObj.entry?.movieId)
    }

    @Test
    fun update() = entryRepoTestApplication(
        func = "update",
        appSettings = appSettings(
            repo = EntryRepoInitialized(
                EntryRepoInMemory(randomUuid = { uuidNew }),
                initObjects = listOf(initialEntry),
            )
        ),
        request = EntryUpdateRequest(
            entry = EntryUpdateObject(
                id = uuidOld,
                movieId = "movie:updated",
                viewingDate = "2026-01-03",
                rating = 10,
                comment = "updated repo entry",
                lock = "lock-$uuidOld",
            ),
            debug = EntryDebug(mode = EntryRequestDebugMode.TEST),
        ),
    ) { response ->
        val responseObj = response.body<EntryUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.entry?.id)
        assertEquals("movie:updated", responseObj.entry?.movieId)
        assertEquals(10, responseObj.entry?.rating)
    }

    @Test
    fun delete() = entryRepoTestApplication(
        func = "delete",
        appSettings = appSettings(
            repo = EntryRepoInitialized(
                EntryRepoInMemory(),
                initObjects = listOf(initialEntry),
            )
        ),
        request = EntryDeleteRequest(
            entry = EntryDeleteObject(
                id = uuidOld,
                lock = "lock-$uuidOld",
            ),
            debug = EntryDebug(mode = EntryRequestDebugMode.TEST),
        ),
    ) { response ->
        val responseObj = response.body<EntryDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.entry?.id)
    }

    @Test
    fun search() = entryRepoTestApplication(
        func = "search",
        appSettings = appSettings(
            repo = EntryRepoInitialized(
                EntryRepoInMemory(),
                initObjects = listOf(initialEntry),
            )
        ),
        request = EntrySearchRequest(
            entryFilter = EntrySearchFilter(searchString = "repo"),
            debug = EntryDebug(mode = EntryRequestDebugMode.TEST),
        ),
    ) { response ->
        val responseObj = response.body<EntrySearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.propertyEntries?.size)
        assertEquals(uuidOld, responseObj.propertyEntries?.first()?.id)
    }

    private fun appSettings(repo: com.funkycorgi.vulpecula.entry.common.repo.IRepoEntry) = EntryAppSettings(
        corSettings = EntryCorSettings(repoTest = repo)
    )

    private fun entryRepoTestApplication(
        func: String,
        appSettings: EntryAppSettings,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(appSettings) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/entry/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
