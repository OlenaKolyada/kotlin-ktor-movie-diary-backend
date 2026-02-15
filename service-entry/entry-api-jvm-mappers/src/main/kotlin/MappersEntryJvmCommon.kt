package com.funkycorgi.vulpecula.entry.api.jvm.mappers

import com.funkycorgi.vulpecula.entry.common.NONE
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import com.funkycorgi.vulpecula.entry.common.models.ViewingDate
import kotlinx.datetime.Instant

internal fun EntryId.toTransportEntry() =
    takeIf { it != EntryId.NONE }?.asString()

internal fun MovieId.toTransportEntry(): String? =
    takeIf { it != MovieId.NONE }?.asString()

internal fun ViewingDate.toTransportEntry(): String? =
    takeIf { it != ViewingDate.NONE }?.asLocalDate()?.toString()

internal fun Int.toTransportEntry(): Int? =
    takeIf { it > 0 }

internal fun Instant.toTransportEntry(): String? =
    takeIf { it != Instant.NONE }?.toString()

internal fun EntryLock.toTransportEntry() =
    takeIf { it != EntryLock.NONE }?.asString()