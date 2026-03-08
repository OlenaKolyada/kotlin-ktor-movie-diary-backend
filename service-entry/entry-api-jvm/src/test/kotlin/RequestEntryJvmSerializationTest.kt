package com.funkycorgi.vulpecula.entry.api.jvm

import com.funkycorgi.vulpecula.entry.api.jvm.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestEntryJvmSerializationTest {
    private val request = EntryCreateRequest(
        requestType = "create",
        debug = EntryDebug(
            mode = EntryRequestDebugMode.STUB,
            stub = EntryRequestDebugStubs.BAD_ID
        ),
        entry = EntryCreateObject(
            movieId = "movie:tt0111161",
            viewingDate = "2025-12-15",
            rating = 7,
            comment = "Nice movie!",
        )
    )

    @Test
    fun serialize() {
        val json = entryApiJvmSerializer.writeValueAsString(request)

        assertContains(json, Regex("\"movieId\":\\s*\"movie:tt0111161\""))
        assertContains(json, Regex("\"viewingDate\":\\s*\"2025-12-15\""))
        assertContains(json, Regex("\"rating\":\\s*7"))
        assertContains(json, Regex("\"comment\":\\s*\"Nice movie!\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badId\""))
    }

    @Test
    fun deserialize() {
        val json = entryApiJvmSerializer.writeValueAsString(request)
        val obj = entryApiJvmSerializer.readValue(json, IRequest::class.java) as EntryCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"entry": null}
        """.trimIndent()
        val obj = entryApiJvmSerializer.readValue(jsonString, EntryCreateRequest::class.java)

        assertEquals(null, obj.entry)
    }
}
