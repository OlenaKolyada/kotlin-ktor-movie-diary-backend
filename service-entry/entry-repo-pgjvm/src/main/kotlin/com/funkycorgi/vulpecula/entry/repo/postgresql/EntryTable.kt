package com.funkycorgi.vulpecula.entry.repo.postgresql

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import com.funkycorgi.vulpecula.entry.common.NONE
import com.funkycorgi.vulpecula.entry.common.models.*

// Sentinel: Instant.NONE cannot be stored in PostgreSQL (out of range); map to/from EPOCH
private val NONE_INSTANT_JAVA: java.time.Instant = java.time.Instant.EPOCH

class EntryTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val userId = text(SqlFields.USER_ID)
    val movieId = text(SqlFields.MOVIE_ID)
    val viewingDate = date(SqlFields.VIEWING_DATE)
    val rating = integer(SqlFields.RATING)
    val comment = text(SqlFields.COMMENT).nullable()
    val lock = text(SqlFields.LOCK)
    val createdAt = timestamp(SqlFields.CREATED_AT)
    val updatedAt = timestamp(SqlFields.UPDATED_AT)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = Entry(
        id = EntryId(res[id]),
        userId = UserId(res[userId]),
        movieId = MovieId(res[movieId]),
        viewingDate = ViewingDate(res[viewingDate].toKotlinLocalDate()),
        rating = res[rating],
        comment = res[comment] ?: "",
        lock = EntryLock(res[lock]),
        createdAt = res[createdAt].let { if (it == NONE_INSTANT_JAVA) Instant.NONE else it.toKotlinInstant() },
        updatedAt = res[updatedAt].let { if (it == NONE_INSTANT_JAVA) Instant.NONE else it.toKotlinInstant() },
    )

    fun UpdateBuilder<*>.to(entry: Entry, randomUuid: () -> String) {
        this[id] = entry.id.takeIf { it != EntryId.NONE }?.asString() ?: randomUuid()
        this[userId] = entry.userId.asString()
        this[movieId] = entry.movieId.asString()
        this[viewingDate] = entry.viewingDate.asLocalDate().toJavaLocalDate()
        this[rating] = entry.rating
        this[comment] = entry.comment.takeIf { it.isNotBlank() }
        this[lock] = entry.lock.takeIf { it != EntryLock.NONE }?.asString() ?: randomUuid()
        this[createdAt] = if (entry.createdAt == Instant.NONE) NONE_INSTANT_JAVA else entry.createdAt.toJavaInstant()
        this[updatedAt] = if (entry.updatedAt == Instant.NONE) NONE_INSTANT_JAVA else entry.updatedAt.toJavaInstant()
    }
}
