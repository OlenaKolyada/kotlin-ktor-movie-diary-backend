package com.funkycorgi.vulpecula.entry.cor

interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}
