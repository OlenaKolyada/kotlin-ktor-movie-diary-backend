package com.funkycorgi.vulpecula.entry.app.ktor.stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import com.funkycorgi.vulpecula.entry.api.jvm.models.*
import com.funkycorgi.vulpecula.entry.app.ktor.EntryAppSettings
import com.funkycorgi.vulpecula.entry.app.ktor.module
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class EntryStubApiTest {

    @Test
    fun create() = entryTestApplication(
        func = "create",
        request = EntryCreateRequest(
            entry = EntryCreateObject(
                movieId = "movie:12345",
                viewingDate = "2025-10-15",
                rating = 9,
                comment = "Потрясающий фильм о надежде",
            ),
            debug = EntryDebug(
                mode = EntryRequestDebugMode.STUB,
                stub = EntryRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<EntryCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("12345", responseObj.entry?.id)
    }

    @Test
    fun read() = entryTestApplication(
        func = "read",
        request = EntryReadRequest(
            entry = EntryReadObject("12345"),
            debug = EntryDebug(
                mode = EntryRequestDebugMode.STUB,
                stub = EntryRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<EntryReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("12345", responseObj.entry?.id)
    }

    @Test
    fun update() = entryTestApplication(
        func = "update",
        request = EntryUpdateRequest(
            entry = EntryUpdateObject(
                id = "12345",
                movieId = "movie:12345",
                viewingDate = "2025-10-15",
                rating = 9,
                comment = "Потрясающий фильм о надежде",
            ),
            debug = EntryDebug(
                mode = EntryRequestDebugMode.STUB,
                stub = EntryRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<EntryUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("12345", responseObj.entry?.id)
    }

    @Test
    fun delete() = entryTestApplication(
        func = "delete",
        request = EntryDeleteRequest(
            entry = EntryDeleteObject(
                id = "12345",
                lock = "123",
            ),
            debug = EntryDebug(
                mode = EntryRequestDebugMode.STUB,
                stub = EntryRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<EntryDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("12345", responseObj.entry?.id)
    }

    @Test
    fun search() = entryTestApplication(
        func = "search",
        request = EntrySearchRequest(
            entryFilter = EntrySearchFilter(),
            debug = EntryDebug(
                mode = EntryRequestDebugMode.STUB,
                stub = EntryRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<EntrySearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("entry-01", responseObj.propertyEntries?.first()?.id)
    }

    private fun entryTestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(EntryAppSettings(corSettings = EntryCorSettings())) }
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
