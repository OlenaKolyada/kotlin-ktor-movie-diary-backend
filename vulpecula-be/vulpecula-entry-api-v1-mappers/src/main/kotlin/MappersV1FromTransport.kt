package com.funkycorgi.vulpecula.entry.mappers.v1

import com.funkycorgi.vulpecula.entry.api.v1.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryCreateRequest
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryDebug
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryDeleteObject
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryDeleteRequest
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryReadObject
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryReadRequest
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryRequestDebugMode
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryRequestDebugStubs
import com.funkycorgi.vulpecula.entry.api.v1.models.EntrySearchFilter
import com.funkycorgi.vulpecula.entry.api.v1.models.EntrySearchRequest
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryUpdateObject
import com.funkycorgi.vulpecula.entry.api.v1.models.EntryUpdateRequest
import com.funkycorgi.vulpecula.entry.api.v1.models.IRequest
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryFilter
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.mappers.v1.exceptions.UnknownRequestClass
import com.funkycorgi.vulpecula.entry.common.models.ViewingDate
import com.funkycorgi.vulpecula.entry.common.models.MovieId
import kotlinx.datetime.LocalDate

fun EntryContext.fromTransport(request: IRequest) = when (request) {
    is EntryCreateRequest -> fromTransport(request)
    is EntryReadRequest -> fromTransport(request)
    is EntryUpdateRequest -> fromTransport(request)
    is EntryDeleteRequest -> fromTransport(request)
    is EntrySearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

fun EntryContext.fromTransport(request: EntryCreateRequest) {
    command = EntryCommand.CREATE
    entryRequest = request.entry?.toInternal() ?: Entry()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EntryContext.fromTransport(request: EntryReadRequest) {
    command = EntryCommand.READ
    entryRequest = request.entry.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EntryContext.fromTransport(request: EntryUpdateRequest) {
    command = EntryCommand.UPDATE
    entryRequest = request.entry?.toInternal() ?: Entry()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EntryContext.fromTransport(request: EntryDeleteRequest) {
    command = EntryCommand.DELETE
    entryRequest = request.entry.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EntryContext.fromTransport(request: EntrySearchRequest) {
    command = EntryCommand.SEARCH
    entryFilterRequest = request.entryFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun EntryCreateObject.toInternal(): Entry = Entry(
    movieId = this.movieId?.toMovieId() ?: MovieId.NONE,
    viewingDate = this.viewingDate?.toViewingDate() ?: ViewingDate.NONE,
    rating = this.rating ?: -1,
    comment = this.comment ?: "",
)

private fun EntryReadObject?.toInternal(): Entry =
    if (this != null) {
        Entry(id = id.toEntryId())
    } else {
        Entry()
}

private fun EntryUpdateObject.toInternal(): Entry = Entry(
    id = this.id.toEntryId(),
    movieId = this.movieId?.toMovieId() ?: MovieId.NONE,
    viewingDate = this.viewingDate?.toViewingDate() ?: ViewingDate.NONE,
    rating = this.rating ?: -1,
    comment = this.comment ?: "",
    lock = lock.toEntryLock(),
)

private fun EntryDeleteObject?.toInternal(): Entry =
    if (this != null) {
        Entry(
            id = id.toEntryId(),
            lock = lock.toEntryLock(),
        )
    } else {
        Entry()
}

private fun EntrySearchFilter?.toInternal(): EntryFilter = EntryFilter(
    searchString = this?.searchString ?: "",
    movieId = this?.movieId?.toMovieId() ?: MovieId.NONE,
    viewingDateFrom = this?.viewingDateFrom?.toViewingDate() ?: ViewingDate.NONE,
    viewingDateTo = this?.viewingDateTo?.toViewingDate() ?: ViewingDate.NONE,
)

private fun EntryDebug?.transportToWorkMode(): EntryWorkMode = when (this?.mode) {
    EntryRequestDebugMode.PROD -> EntryWorkMode.PROD
    EntryRequestDebugMode.TEST -> EntryWorkMode.TEST
    EntryRequestDebugMode.STUB -> EntryWorkMode.STUB
    null -> EntryWorkMode.PROD
}

private fun EntryDebug?.transportToStubCase(): EntryStubs = when (this?.stub) {
    EntryRequestDebugStubs.SUCCESS -> EntryStubs.SUCCESS
    EntryRequestDebugStubs.NOT_FOUND -> EntryStubs.NOT_FOUND
    EntryRequestDebugStubs.BAD_ID -> EntryStubs.BAD_ID
    EntryRequestDebugStubs.BAD_MOVIE_ID -> EntryStubs.BAD_MOVIE_ID
    EntryRequestDebugStubs.BAD_RATING -> EntryStubs.BAD_RATING
    EntryRequestDebugStubs.BAD_VIEWING_DATE -> EntryStubs.BAD_VIEWING_DATE
    EntryRequestDebugStubs.BAD_COMMENT -> EntryStubs.BAD_COMMENT
    EntryRequestDebugStubs.CANNOT_DELETE -> EntryStubs.CANNOT_DELETE
    EntryRequestDebugStubs.DB_ERROR -> EntryStubs.DB_ERROR
    EntryRequestDebugStubs.BAD_SEARCH_STRING -> EntryStubs.BAD_SEARCH_STRING
    null -> EntryStubs.NONE
}

private fun String?.toEntryId() =
    this?.let { EntryId(it) } ?: EntryId.NONE

private fun String.toMovieId() =
    MovieId(this)

private fun String.toViewingDate() =
    ViewingDate(LocalDate.parse(this))

private fun String?.toEntryLock() =
    this?.let { EntryLock(it) } ?: EntryLock.NONE

//private fun String?.toEntryWithId() = Entry(id = this.toEntryId())