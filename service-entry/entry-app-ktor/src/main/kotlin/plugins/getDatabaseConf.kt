package com.funkycorgi.vulpecula.entry.app.ktor.plugins

import io.ktor.server.application.*
import com.funkycorgi.vulpecula.entry.app.ktor.configs.ConfigPaths
import com.funkycorgi.vulpecula.entry.app.ktor.configs.PostgresConfig
import com.funkycorgi.vulpecula.entry.common.repo.IRepoEntry
import com.funkycorgi.vulpecula.entry.repo.inmemory.EntryRepoInMemory
import com.funkycorgi.vulpecula.entry.repo.postgresql.RepoEntrySql
import com.funkycorgi.vulpecula.entry.repo.postgresql.SqlProperties

enum class EntryDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.getDatabaseConf(type: EntryDbType): IRepoEntry {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yaml to one of: 'inmemory', 'postgres'"
        )
    }
}

fun Application.initInMemory(): IRepoEntry = EntryRepoInMemory()

fun Application.initPostgres(): IRepoEntry {
    val config = PostgresConfig(environment.config)
    return RepoEntrySql(
        properties = SqlProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
            schema = config.schema,
            database = config.database,
        ),
    )
}
