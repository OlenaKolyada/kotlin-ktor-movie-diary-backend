package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.models.EntryError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<EntryError>
}
