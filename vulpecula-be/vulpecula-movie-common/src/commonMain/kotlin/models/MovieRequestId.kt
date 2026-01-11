package com.funkycorgi.vulpecula.movie.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MovieRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MovieRequestId("")
    }
}
