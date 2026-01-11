package com.funkycorgi.vulpecula.movie.common.models

data class Movie(
    var id: MovieId = MovieId.NONE,
    var title: String = "",
    var releaseYear: Int = 0,
    var director: List<String> = emptyList(),
    var cast: List<String> = emptyList(),
    var genre: List<String> = emptyList(),
    var synopsis: String = "",
    var posterUrl: String = "",
    var tmdbId: Int = 0,
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Movie()
    }
}
