package com.funkycorgi.vulpecula.entry.api.kmp

import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryCreateRequest
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryDebug
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryRequestDebugMode
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryRequestDebugStubs
import com.funkycorgi.vulpecula.entry.api.kmp.models.IRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestEntryKmpSerializationTest {
    private val request: IRequest = EntryCreateRequest(
        debug = EntryDebug(
            mode = EntryRequestDebugMode.STUB,
            stub = EntryRequestDebugStubs.BAD_RATING
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
        val json = entryApiKmpMapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"movieId\":\\s*\"movie:tt0111161\""))
        assertContains(json, Regex("\"viewingDate\":\\s*\"2025-12-15\""))
        assertContains(json, Regex("\"rating\":\\s*7"))
        assertContains(json, Regex("\"comment\":\\s*\"Nice movie!\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badId\""))
    }

    @Test
    fun deserialize() {
        val json = entryApiKmpMapper.encodeToString(request)
        val obj = entryApiKmpMapper.decodeFromString<IRequest>(json) as EntryCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"entry": null}
        """.trimIndent()
        val obj = entryApiKmpMapper.decodeFromString<EntryCreateRequest>(jsonString)

        assertEquals(null, obj.entry)
    }
}
