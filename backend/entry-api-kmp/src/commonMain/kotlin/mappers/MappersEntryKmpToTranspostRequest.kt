package com.funkycorgi.vulpecula.entry.api.kmp.mappers

import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryDeleteObject
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryReadObject
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryUpdateObject
import com.funkycorgi.vulpecula.entry.common.models.Entry

fun Entry.toTransportCreateAd() = EntryCreateObject(
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