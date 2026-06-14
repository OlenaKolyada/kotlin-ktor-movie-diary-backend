package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryId

val errorEmptyId = DbEntryResponseErr(
    EntryError(
        code = "repo-empty-id",
        group = "repo",
        field = "id",
        message = "Id must not be empty",
    )
)

fun errorNotFound(id: EntryId) = DbEntryResponseErr(
    EntryError(
        code = "repo-not-found",
        group = "repo",
        field = "id",
        message = "Object with id ${id.asString()} is not found",
    )
)
