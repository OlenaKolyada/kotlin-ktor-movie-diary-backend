package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.models.EntryFilter
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.models.ViewingDate

data class DbEntryFilterRequest(
    val searchString: String = "",
    val movieId: MovieId = MovieId.NONE,
    val viewingDateFrom: ViewingDate = ViewingDate.NONE,
    val viewingDateTo: ViewingDate = ViewingDate.NONE,
) {
    constructor(filter: EntryFilter) : this(
        searchString = filter.searchString,
        movieId = filter.movieId,
        viewingDateFrom = filter.viewingDateFrom,
        viewingDateTo = filter.viewingDateTo,
    )
}
