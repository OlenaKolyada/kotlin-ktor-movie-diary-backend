package com.funkycorgi.vulpecula.entry.api.jvm

import com.funkycorgi.vulpecula.entry.api.jvm.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseEntryJvmSerializationTest {
    private val response = EntryCreateResponse(
        responseType = "create",
        entry = EntryResponseObject(
            movieId = "movie:tt0111161",
            viewingDate = "2025-12-15",
            rating = 7,
            comment = "Nice movie!",
        )
    )

    @Test
    fun serialize() {
        val json = entryApiJvmSerializer.writeValueAsString(response)

        assertContains(json, Regex("\"movieId\":\\s*\"movie:tt0111161\""))
        assertContains(json, Regex("\"viewingDate\":\\s*\"2025-12-15\""))
        assertContains(json, Regex("\"rating\":\\s*7"))
        assertContains(json, Regex("\"comment\":\\s*\"Nice movie!\""))
    }

    @Test
    fun deserialize() {
        val json = entryApiJvmSerializer.writeValueAsString(response)
        val obj = entryApiJvmSerializer.readValue(json, IResponse::class.java) as EntryCreateResponse

        assertEquals(response, obj)
    }
}
