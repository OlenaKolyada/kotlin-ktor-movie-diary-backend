package com.funkycorgi.vulpecula.entry.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class EntryRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = EntryRequestId("")
    }
}
