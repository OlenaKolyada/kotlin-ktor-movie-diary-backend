package com.funkycorgi.vulpecula.entry.stubs

import com.funkycorgi.vulpecula.entry.common.models.*
import kotlinx.datetime.LocalDate

object EntryStubObjects {
    val ENTRY_SUCCESS: Entry
        get() = Entry(
            id = EntryId("12345"),
            userId = UserId("user:12345"),
            movieId = MovieId("movie:12345"),
            viewingDate = ViewingDate(LocalDate(2025, 10, 15)),
            rating = 9,
            comment = "Потрясающий фильм о надежде",
            lock = EntryLock("123"),
            userPermissions = mutableSetOf(
                EntryUserPermission.READ,
                EntryUserPermission.UPDATE,
                EntryUserPermission.DELETE,
            )
        )

    val ENTRY_NOT_FOUND: Entry
        get() = Entry(
            id = EntryId("not-found")
        )

    val ENTRY_BAD_ID: Entry
        get() = Entry(
            id = EntryId("bad-id")
        )

    val ENTRY_BAD_MOVIE_ID: Entry
        get() = Entry(
            movieId = MovieId("bad-movie-id")
        )

    val ENTRY_BAD_RATING: Entry
        get() = Entry(
            rating = -1
        )

    // не забыть добавить валидацию в логику
    val ENTRY_BAD_VIEWING_DATE: Entry
        get() = Entry(
            viewingDate = ViewingDate(LocalDate(1800, 1, 1))
        )

    val ENTRY_BAD_COMMENT: Entry
        get() = Entry(
            comment = "0"
        )

    val ENTRY_CANNOT_DELETE: Entry
        get() = Entry(
            id = EntryId("cannot-delete-id"),
            lock = EntryLock("locked-123")
        )

    val ENTRY_DB_ERROR: Entry
        get() = Entry(
            id = EntryId("db-error-id")
        )
}