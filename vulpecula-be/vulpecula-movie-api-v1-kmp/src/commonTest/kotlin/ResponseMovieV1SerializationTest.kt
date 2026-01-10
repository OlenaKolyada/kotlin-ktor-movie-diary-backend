package com.funkycorgi.vulpecula.movie.api.v1

import com.funkycorgi.vulpecula.movie.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseMovieV1SerializationTest {
    private val response: IResponse = MovieSearchResponse(
        movies = listOf(
            MovieResponseObject(
                id = "movie:tt0111161",
                title = "The Shawshank Redemption",
                releaseYear = 1994,
                genre = listOf("Drama"),
                synopsis = "Two imprisoned men bond over a number of years...",
                posterUrl = "https://example.com/poster.jpg",
                tmdbId = 278
            )
        )
    )

    @Test
    fun serialize() {
//        val json = apiMovieV1Mapper.encodeToString(MovieRequestSerializer, request)
//        val json = apiMovieV1Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiMovieV1Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"id\":\\s*\"movie:tt0111161\""))
        assertContains(json, Regex("\"title\":\\s*\"The Shawshank Redemption\""))
        assertContains(json, Regex("\"releaseYear\":\\s*1994"))
    }

    @Test
    fun deserialize() {
        val json = apiMovieV1Mapper.encodeToString(response)
        val obj = apiMovieV1Mapper.decodeFromString<IResponse>(json) as MovieSearchResponse

        assertEquals(response, obj)
    }
}
