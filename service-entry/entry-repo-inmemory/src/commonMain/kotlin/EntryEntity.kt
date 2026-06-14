package com.funkycorgi.vulpecula.entry.repo.inmemory

import com.funkycorgi.vulpecula.entry.common.models.*
import kotlinx.datetime.LocalDate

data class EntryEntity(
    val id: String? = null,
    val userId: String? = null,
    val movieId: String? = null,
    val viewingDate: LocalDate? = null,
    val rating: Int? = null,
    val comment: String? = null,
    val lock: String? = null,
) {
    constructor(model: Entry) : this(
        id = model.id.takeIf { it != EntryId.NONE }?.asString(),
        userId = model.userId.takeIf { it != UserId.NONE }?.asString(),
        movieId = model.movieId.takeIf { it != MovieId.NONE }?.asString(),
        viewingDate = model.viewingDate.takeIf { it != ViewingDate.NONE }?.asLocalDate(),
        rating = model.rating,
        comment = model.comment.takeIf { it.isNotBlank() },
        lock = model.lock.takeIf { it != EntryLock.NONE }?.asString(),
    )

    fun toInternal() = Entry(
        id = id?.let { EntryId(it) } ?: EntryId.NONE,
        userId = userId?.let { UserId(it) } ?: UserId.NONE,
        movieId = movieId?.let { MovieId(it) } ?: MovieId.NONE,
        viewingDate = viewingDate?.let { ViewingDate(it) } ?: ViewingDate.NONE,
        rating = rating ?: 0,
        comment = comment ?: "",
        lock = lock?.let { EntryLock(it) } ?: EntryLock.NONE,
    )
}
