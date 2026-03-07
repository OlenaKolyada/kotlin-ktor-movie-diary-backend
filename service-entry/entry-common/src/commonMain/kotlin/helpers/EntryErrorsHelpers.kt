package com.funkycorgi.vulpecula.entry.common.helpers

import com.funkycorgi.vulpecula.entry.common.models.EntryError

fun Throwable.asEntryError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = EntryError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
