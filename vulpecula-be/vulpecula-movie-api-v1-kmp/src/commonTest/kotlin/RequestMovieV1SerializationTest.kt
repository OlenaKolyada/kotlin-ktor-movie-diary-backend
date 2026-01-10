package com.funkycorgi.vulpecula.movie.api.v1

import com.funkycorgi.vulpecula.movie.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestMovieV1SerializationTest {
    private val request: IRequest = MovieSearchRequest(
        debug = MovieDebug(
            mode = MovieRequestDebugMode.STUB,
            stub = MovieRequestDebugStubs.SUCCESS
        ),
        movieFilter = MovieSearchFilter(
            searchString = "Shawshank"
        )
    )

    @Test
    fun serialize() {
        val json = apiMovieV1Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"searchString\":\\s*\"Shawshank\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"success\""))
    }

    @Test
    fun deserialize() {
        val json = apiMovieV1Mapper.encodeToString(request)
        val obj = apiMovieV1Mapper.decodeFromString<IRequest>(json) as MovieSearchRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"movieFilter": null}
        """.trimIndent()
        val obj = apiMovieV1Mapper.decodeFromString<MovieSearchRequest>(jsonString)

        assertEquals(null, obj.movieFilter)
    }
}
