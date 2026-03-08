package com.funkycorgi.vulpecula.entry.api.jvm.mappers

import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryCreateResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryDeleteResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryReadResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryResponseObject
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntrySearchResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryUpdateResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryPermissions
import com.funkycorgi.vulpecula.entry.api.jvm.models.Error
import com.funkycorgi.vulpecula.entry.api.jvm.models.IResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.ResponseResult
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.exceptions.UnknownEntryCommand
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryUserPermission
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.UserId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.models.MovieId

fun EntryContext.toTransportEntry(): IResponse = when (val cmd = command) {
    EntryCommand.CREATE -> toTransportCreate()
    EntryCommand.READ -> toTransportRead()
    EntryCommand.UPDATE -> toTransportUpdate()
    EntryCommand.DELETE -> toTransportDelete()
    EntryCommand.SEARCH -> toTransportSearch()
    EntryCommand.NONE -> throw UnknownEntryCommand(cmd)
}

fun EntryContext.toTransportCreate() = EntryCreateResponse(
    responseType = "create",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry(),
)

fun EntryContext.toTransportRead() = EntryReadResponse(
    responseType = "read",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry()
)

fun EntryContext.toTransportUpdate() = EntryUpdateResponse(
    responseType = "update",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry()
)

fun EntryContext.toTransportDelete() = EntryDeleteResponse(
    responseType = "delete",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    entry = entryResponse.toTransportEntry()
)

fun EntryContext.toTransportSearch() = EntrySearchResponse(
    responseType = "search",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    propertyEntries = entriesResponse.toTransportEntry()
)

fun Entry.toTransportEntry(): EntryResponseObject = EntryResponseObject(
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

private fun EntryState.toResult(): ResponseResult? = when (this) {
    EntryState.RUNNING -> ResponseResult.SUCCESS
    EntryState.FAILING -> ResponseResult.ERROR
    EntryState.FINISHING -> ResponseResult.SUCCESS
    EntryState.NONE -> null
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

fun List<Entry>.toTransportEntry(): List<EntryResponseObject>? = this
    .map { it.toTransportEntry() }
    .toList()
    .takeIf { it.isNotEmpty() }