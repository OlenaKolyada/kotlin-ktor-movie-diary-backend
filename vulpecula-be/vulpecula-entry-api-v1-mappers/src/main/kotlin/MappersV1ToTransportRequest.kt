package com.funkycorgi.vulpecula.entry.mappers.v1

import com.funkycorgi.vulpecula.entry.api.v1.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryDeleteObject
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryReadObject
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryUpdateObject
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.models.ViewingDate

fun Entry.toTransportCreateEntry() = EntryCreateObject(
    movieId = movieId.toTransportEntry(),
    viewingDate = viewingDate.toTransportEntry(),
    rating = rating,
    comment = comment,
)

fun Entry.toTransportReadEntry() = EntryReadObject(
    id = id.toTransportEntry()
)

fun Entry.toTransportUpdateEntry() = EntryUpdateObject(
    id = id.toTransportEntry(),
    movieId = movieId.toTransportEntry(),
    viewingDate = viewingDate.toTransportEntry(),
    rating = rating,
    comment = comment,
    lock = lock.toTransportEntry(),
)

internal fun EntryLock.toTransportEntry() = takeIf { it != EntryLock.NONE }?.asString()

fun MovieId.toTransportEntry(): String? = takeIf { it != MovieId.NONE }?.asString()

private fun ViewingDate.toTransportEntry() = takeIf { it != ViewingDate.NONE }?.asLocalDate()?.toString()

fun Entry.toTransportDeleteEntry() = EntryDeleteObject(
    id = id.toTransportEntry(),
    lock = lock.toTransportEntry(),
)
