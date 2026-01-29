package com.funkycorgi.vulpecula.entry.mappers

import com.funkycorgi.vulpecula.entry.api.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.models.EntryDeleteObject
import com.funkycorgi.vulpecula.entry.api.models.EntryReadObject
import com.funkycorgi.vulpecula.entry.api.models.EntryUpdateObject
import com.funkycorgi.vulpecula.entry.common.models.Entry

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

fun Entry.toTransportDeleteEntry() = EntryDeleteObject(
    id = id.toTransportEntry(),
    lock = lock.toTransportEntry(),
)
