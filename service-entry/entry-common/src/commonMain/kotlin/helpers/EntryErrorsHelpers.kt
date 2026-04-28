package com.funkycorgi.vulpecula.entry.common.helpers

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryState

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

fun EntryContext.addError(vararg error: EntryError) = errors.addAll(error)

fun EntryContext.fail(error: EntryError) {
    addError(error)
    state = EntryState.FAILING
}
