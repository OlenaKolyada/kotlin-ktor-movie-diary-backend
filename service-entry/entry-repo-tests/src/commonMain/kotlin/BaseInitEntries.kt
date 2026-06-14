package com.funkycorgi.vulpecula.entry.repo.tests

import com.funkycorgi.vulpecula.entry.common.models.*
import kotlinx.datetime.LocalDate

abstract class BaseInitEntries(
    private val operation: String,
) : IInitObjects {
    fun createInitTestModel(suffix: String) = Entry(
        id = EntryId("entry-repo-$operation-$suffix"),
        userId = UserId("user-repo-$operation-$suffix"),
        movieId = MovieId("movie-repo-$operation-$suffix"),
        viewingDate = ViewingDate(LocalDate(2026, 1, 10)),
        rating = 8,
        comment = "repo $operation $suffix comment",
        lock = EntryLock("lock-repo-$operation-$suffix"),
    )
}
