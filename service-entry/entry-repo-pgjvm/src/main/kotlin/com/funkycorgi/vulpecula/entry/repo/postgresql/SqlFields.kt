package com.funkycorgi.vulpecula.entry.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val USER_ID = "user_id"
    const val MOVIE_ID = "movie_id"
    const val VIEWING_DATE = "viewing_date"
    const val RATING = "rating"
    const val COMMENT = "comment"
    const val LOCK = "lock"
    const val CREATED_AT = "created_at"
    const val UPDATED_AT = "updated_at"

    const val FILTER_MOVIE_ID = MOVIE_ID

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, USER_ID, MOVIE_ID, VIEWING_DATE, RATING, COMMENT, LOCK, CREATED_AT, UPDATED_AT,
    )
}
