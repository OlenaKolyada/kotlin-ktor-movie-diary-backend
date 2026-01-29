package com.funkycorgi.vulpecula.entry.common.models

data class EntryError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
