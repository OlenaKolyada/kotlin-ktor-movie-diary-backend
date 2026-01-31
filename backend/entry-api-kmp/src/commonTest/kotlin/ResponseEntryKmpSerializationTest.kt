package com.funkycorgi.vulpecula.entry.api.kmp

import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryCreateResponse
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryResponseObject
import com.funkycorgi.vulpecula.entry.api.kmp.models.IResponse
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseEntryKmpSerializationTest {
    private val response: IResponse = EntryCreateResponse(
        entry = EntryResponseObject(
            movieId = "movie:tt0111161",
            viewingDate = "2025-12-15",
            rating = 7,
            comment = "Nice movie!",
        )
    )

    @Test
    fun serialize() {
        val json = entryApiKmpMapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"movieId\":\\s*\"movie:tt0111161\""))
        assertContains(json, Regex("\"viewingDate\":\\s*\"2025-12-15\""))
        assertContains(json, Regex("\"rating\":\\s*7"))
        assertContains(json, Regex("\"comment\":\\s*\"Nice movie!\""))
    }

    @Test
    fun deserialize() {
        val json = entryApiKmpMapper.encodeToString(response)
        val obj = entryApiKmpMapper.decodeFromString<IResponse>(json) as EntryCreateResponse

        assertEquals(response, obj)
    }
}
