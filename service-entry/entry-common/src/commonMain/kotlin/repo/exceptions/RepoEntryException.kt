package com.funkycorgi.vulpecula.entry.common.repo.exceptions

import com.funkycorgi.vulpecula.entry.common.models.EntryId

open class RepoEntryException(
    id: EntryId,
    message: String,
    cause: Throwable? = null,
) : RepoException(
    message = "Entry ${id.asString()}: $message",
    cause = cause,
)
