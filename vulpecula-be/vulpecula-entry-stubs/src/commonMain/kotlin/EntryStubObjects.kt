package com.funkycorgi.vulpecula.entry.stubs

import com.funkycorgi.vulpecula.entry.common.models.*

object EntryStubObjects {
    val ENTRY_SUCCESS: Entry
        get() = Entry(
            id = EntryId("446655440000"),
            userId = UserId("user:12345"),
            movieId = MovieId("movie:tt0111161"),
            viewingDate = ViewingDate("2025-10-15"),
            rating = Rating(9),
            comment = Comment("Потрясающий фильм о надежде"),
            lock = EntryLock("123"),
            permissionsClient = mutableSetOf(
                EntryUserPermission.READ,
                EntryUserPermission.UPDATE,
                EntryUserPermission.DELETE,
            )
        )

    val ENTRY_NOT_FOUND: Entry
        get() = Entry(
            id = EntryId("not-found-id")
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
            rating = Rating(-1)
        )

    val ENTRY_BAD_VIEWING_DATE: Entry
        get() = Entry(
            viewingDate = ViewingDate("bad-viewing-date")
        )

    val ENTRY_BAD_COMMENT: Entry
        get() = Entry(
            comment = Comment("0")
        )

    val ENTRY_CANNOT_DELETE: Entry
        get() = Entry(
            id = EntryId("cannot-delete-id"),
            lock = EntryLock("locked-123")
        )

    val ENTRY_BAD_SEARCH_STRING: Entry
        get() = Entry()

    val ENTRY_DB_ERROR: Entry
        get() = Entry(
            id = EntryId("db-error-id")
        )
}