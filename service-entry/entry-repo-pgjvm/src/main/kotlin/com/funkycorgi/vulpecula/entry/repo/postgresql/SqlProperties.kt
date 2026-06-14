package com.funkycorgi.vulpecula.entry.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "vulpecula-pass",
    val database: String = "vulpecula_entries",
    val schema: String = "public",
    val table: String = "entries",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
