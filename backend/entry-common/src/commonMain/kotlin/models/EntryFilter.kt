package com.funkycorgi.vulpecula.entry.common.models

data class EntryFilter(
    var searchString: String = "",
    var movieId: MovieId = MovieId.NONE,
    var viewingDateFrom: ViewingDate = ViewingDate.NONE,
    var viewingDateTo: ViewingDate = ViewingDate.NONE,
)
