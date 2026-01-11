package com.funkycorgi.vulpecula.entry.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class EntryId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = EntryId("")
    }
}
