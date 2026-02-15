package com.funkycorgi.vulpecula.entry.api.jvm.mappers
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryDeleteObject
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryReadObject
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryUpdateObject
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
