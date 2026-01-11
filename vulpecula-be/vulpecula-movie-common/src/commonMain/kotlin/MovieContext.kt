package com.funkycorgi.vulpecula.movie.common

import kotlinx.datetime.Instant
import com.funkycorgi.vulpecula.movie.common.models.*
import com.funkycorgi.vulpecula.movie.common.stubs.MovieStubs

data class MovieContext(
    var command: MovieCommand = MovieCommand.NONE,
    var state: MovieState = MovieState.NONE,
    val errors: MutableList<MovieError> = mutableListOf(),

    var workMode: MovieWorkMode = MovieWorkMode.PROD,
    var stubCase: MovieStubs = MovieStubs.NONE,

    var requestId: MovieRequestId = MovieRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var movieRequest: Movie = Movie(),
    var movieFilterRequest: MovieFilter = MovieFilter(),

    var movieResponse: Movie = Movie(),
    var moviesResponse: MutableList<Movie> = mutableListOf(),
)
