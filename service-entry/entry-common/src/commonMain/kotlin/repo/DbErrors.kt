package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.helpers.errorSystem
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.repo.exceptions.RepoConcurrencyException
import com.funkycorgi.vulpecula.entry.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

val errorEmptyId = DbEntryResponseErr(
    EntryError(
        code = "repo-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be empty",
    )
)

fun errorNotFound(id: EntryId) = DbEntryResponseErr(
    EntryError(
        code = "repo-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with id ${id.asString()} is not found",
    )
)

fun errorRepoConcurrency(
    oldEntry: Entry,
    expectedLock: EntryLock,
    exception: Exception = RepoConcurrencyException(
        id = oldEntry.id,
        expectedLock = expectedLock,
        actualLock = oldEntry.lock,
    ),
) = DbEntryResponseErrWithData(
    entry = oldEntry,
    error = EntryError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldEntry.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: EntryId) = DbEntryResponseErr(
    EntryError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Entry ${id.asString()} is empty that is not admitted",
    )
)

fun errorDb(e: RepoException) = DbEntryResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e,
    )
)
