package com.funkycorgi.vulpecula.entry.common.models

@JvmInline
value class ViewingDate(private val date: LocalDate) {
    fun asLocalDate() = date

    companion object {
        val NONE = ViewingDate(LocalDate(1900, 1, 1))
    }
}