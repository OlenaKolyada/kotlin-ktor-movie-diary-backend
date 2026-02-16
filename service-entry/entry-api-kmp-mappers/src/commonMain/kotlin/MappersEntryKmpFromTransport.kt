package com.funkycorgi.vulpecula.entry.api.kmp.mappers

import com.funkycorgi.vulpecula.entry.api.kmp.models.*
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import kotlinx.datetime.LocalDate

fun EntryContext.fromTransport(request: IRequest) = when (request) {
    is EntryCreateRequest -> fromTransport(request)
    is EntryReadRequest -> fromTransport(request)
    is EntryUpdateRequest -> fromTransport(request)
    is EntryDeleteRequest -> fromTransport(request)
    is EntrySearchRequest -> fromTransport(request)
}

private fun String?.toEntryId() = this?.let { EntryId(it) } ?: EntryId.NONE
private fun String?.toEntryLock() = this?.let { EntryLock(it) } ?: EntryLock.NONE
private fun EntryReadObject?.toEntry() = if (this != null) {
    Entry(id = id.toEntryId())
} else {
    Entry()
}
private fun String?.toMovieId() = this?.let { MovieId(it) } ?: MovieId.NONE

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
    EntryRequestDebugStubs.BAD_VIEWING_DATE -> EntryStubs.BAD_VIEWING_DATE
    EntryRequestDebugStubs.BAD_RATING -> EntryStubs.BAD_RATING
    EntryRequestDebugStubs.BAD_COMMENT -> EntryStubs.BAD_COMMENT
    EntryRequestDebugStubs.CANNOT_DELETE -> EntryStubs.CANNOT_DELETE
    EntryRequestDebugStubs.DB_ERROR -> EntryStubs.DB_ERROR
    EntryRequestDebugStubs.BAD_SEARCH_STRING -> EntryStubs.BAD_SEARCH_STRING
    null -> EntryStubs.NONE
}

fun EntryContext.fromTransport(request: EntryCreateRequest) {
    command = EntryCommand.CREATE
    entryRequest = request.entry?.toEntry() ?: Entry()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EntryContext.fromTransport(request: EntryReadRequest) {
    command = EntryCommand.READ
    entryRequest = request.entry.toEntry()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EntryContext.fromTransport(request: EntryUpdateRequest) {
    command = EntryCommand.UPDATE
    entryRequest = request.entry?.toEntry() ?: Entry()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EntryContext.fromTransport(request: EntryDeleteRequest) {
    command = EntryCommand.DELETE
    entryRequest = request.entry.toEntry()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun EntryDeleteObject?.toEntry(): Entry = if (this != null) {
    Entry(
        id = id.toEntryId(),
        lock = lock.toEntryLock(),
    )
} else {
    Entry()
}

fun EntryContext.fromTransport(request: EntrySearchRequest) {
    command = EntryCommand.SEARCH
    entryFilterRequest = request.entryFilter.toEntry()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun EntrySearchFilter?.toEntry(): EntryFilter = EntryFilter(
    searchString = this?.searchString ?: ""
)

private fun EntryCreateObject.toEntry(): Entry = Entry(
    movieId = this.movieId?.toMovieId() ?: MovieId.NONE,
    viewingDate = this.viewingDate?.toViewingDate() ?: ViewingDate.NONE,
    rating = this.rating ?: -1,
    comment = this.comment ?: "",
)

private fun EntryUpdateObject.toEntry(): Entry = Entry(
    id = this.id.toEntryId(),
    movieId = this.movieId?.toMovieId() ?: MovieId.NONE,
    viewingDate = this.viewingDate?.toViewingDate() ?: ViewingDate.NONE,
    rating = this.rating ?: -1,
    comment = this.comment ?: "",
    lock = lock.toEntryLock(),
)

private fun String.toViewingDate() =
    ViewingDate(LocalDate.parse(this))
