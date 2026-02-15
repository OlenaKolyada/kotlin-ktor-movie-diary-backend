package com.funkycorgi.vulpecula.entry.api.kmp.mappers

import com.funkycorgi.vulpecula.entry.common.exceptions.UnknownEntryCommand
import com.funkycorgi.vulpecula.entry.api.kmp.models.*
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.*

fun EntryContext.toTransportAd(): IResponse = when (val cmd = command) {
    EntryCommand.CREATE -> toTransportCreate()
    EntryCommand.READ -> toTransportRead()
    EntryCommand.UPDATE -> toTransportUpdate()
    EntryCommand.DELETE -> toTransportDelete()
    EntryCommand.SEARCH -> toTransportSearch()
    EntryCommand.NONE -> throw UnknownEntryCommand(cmd)
}

fun EntryContext.toTransportCreate() = EntryCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry()
)

fun EntryContext.toTransportRead() = EntryReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry()
)

fun EntryContext.toTransportUpdate() = EntryUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry()
)

fun EntryContext.toTransportDelete() = EntryDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry()
)

fun EntryContext.toTransportSearch() = EntrySearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    propertyEntries = entriesResponse.toTransportEntry()
)

fun List<Entry>.toTransportEntry(): List<EntryResponseObject>? = this
    .map { it.toTransportEntry() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun Entry.toTransportEntry(): EntryResponseObject = EntryResponseObject(
    id = id.toTransportEntry(),
    userId = userId.takeIf { it != UserId.NONE }?.asString(),
    movieId = movieId.takeIf { it != MovieId.NONE }?.asString(),
    viewingDate = viewingDate.toTransportEntry(),
    rating = rating.toTransportEntry(),
    comment = comment.takeIf { it.isNotBlank() },
    permissions = userPermissions.toTransportEntry(),
    lock = lock.takeIf { it != EntryLock.NONE }?.asString(),
    createdAt = createdAt.toTransportEntry(),
    updatedAt = updatedAt.toTransportEntry(),
)

private fun Set<EntryUserPermission>.toTransportEntry(): Set<EntryPermissions>? = this
    .map { it.toTransportEntry() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun EntryUserPermission.toTransportEntry() = when (this) {
    EntryUserPermission.CREATE -> EntryPermissions.CREATE
    EntryUserPermission.READ -> EntryPermissions.READ
    EntryUserPermission.UPDATE -> EntryPermissions.UPDATE
    EntryUserPermission.DELETE -> EntryPermissions.DELETE
}

private fun List<EntryError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportEntry() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun EntryError.toTransportEntry() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun EntryState.toResult(): ResponseResult? = when (this) {
    EntryState.RUNNING, EntryState.FINISHING -> ResponseResult.SUCCESS
    EntryState.FAILING -> ResponseResult.ERROR
    EntryState.NONE -> null
}
