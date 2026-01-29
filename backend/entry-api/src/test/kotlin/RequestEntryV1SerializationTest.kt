package com.funkycorgi.vulpecula.entry.api

import com.funkycorgi.vulpecula.entry.api.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestEntryV1SerializationTest {
    private val request = EntryCreateRequest(
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
        val json = apiEntryV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"movieId\":\\s*\"movie:tt0111161\""))
        assertContains(json, Regex("\"viewingDate\":\\s*\"2025-12-15\""))
        assertContains(json, Regex("\"rating\":\\s*7"))
        assertContains(json, Regex("\"comment\":\\s*\"Nice movie!\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badId\""))
    }

    @Test
    fun deserialize() {
        val json = apiEntryV1Mapper.writeValueAsString(request)
        val obj = apiEntryV1Mapper.readValue(json, IRequest::class.java) as EntryCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"entry": null}
        """.trimIndent()
        val obj = apiEntryV1Mapper.readValue(jsonString, EntryCreateRequest::class.java)

        assertEquals(null, obj.entry)
    }
}
