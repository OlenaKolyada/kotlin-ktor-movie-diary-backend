package com.funkycorgi.vulpecula.entry.common.models

import kotlinx.datetime.LocalDate
import kotlin.jvm.JvmInline

@JvmInline
value class ViewingDate(private val date: LocalDate) {
    fun asLocalDate() = date

    companion object {
        val NONE = ViewingDate(LocalDate(1900, 1, 1))
    }
}