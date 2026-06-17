package com.funkycorgi.vulpecula.entry.repo.postgresql

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toJavaLocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.transaction
import com.funkycorgi.vulpecula.entry.common.helpers.asEntryError
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.repo.*
import com.funkycorgi.vulpecula.entry.common.repo.errorNotFound
import com.funkycorgi.vulpecula.entry.repo.common.IRepoEntryInitializable

class RepoEntrySql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : IRepoEntry, IRepoEntryInitializable {
    private val entryTable = EntryTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        entryTable.deleteAll()
    }

    private fun saveObj(entry: Entry): Entry = transaction(conn) {
        val res = entryTable
            .insert {
                it.to(entry, randomUuid)
            }
            .resultedValues
            ?.map { entryTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbEntryResponse): IDbEntryResponse =
        transactionWrapper(block) { DbEntryResponseErr(it.asEntryError()) }

    override fun save(entries: Collection<Entry>): Collection<Entry> = entries.map { saveObj(it) }
    override suspend fun createEntry(rq: DbEntryRequest): IDbEntryResponse = transactionWrapper {
        DbEntryResponseOk(saveObj(rq.entry))
    }

    private fun read(id: EntryId): IDbEntryResponse {
        val res = entryTable.selectAll().where {
            entryTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbEntryResponseOk(entryTable.from(res))
    }

    override suspend fun readEntry(rq: DbEntryIdRequest): IDbEntryResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: EntryId,
        lock: EntryLock,
        block: (Entry) -> IDbEntryResponse
    ): IDbEntryResponse =
        transactionWrapper {
            if (id == EntryId.NONE) return@transactionWrapper errorEmptyId

            val current = entryTable.selectAll().where { entryTable.id eq id.asString() }
                .singleOrNull()
                ?.let { entryTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }

    override suspend fun updateEntry(rq: DbEntryRequest): IDbEntryResponse = update(rq.entry.id, rq.entry.lock) {
        entryTable.updateReturning(where = { entryTable.id eq rq.entry.id.asString() }) {
            it.to(rq.entry.copy(lock = EntryLock(randomUuid())), randomUuid)
        }.singleOrNull()
            ?.let { DbEntryResponseOk(entryTable.from(it)) }
            ?: errorNotFound(rq.entry.id)
    }

    override suspend fun deleteEntry(rq: DbEntryIdRequest): IDbEntryResponse = update(rq.id, rq.lock) {
        entryTable.deleteWhere { id eq rq.id.asString() }
        DbEntryResponseOk(it)
    }

    override suspend fun searchEntry(rq: DbEntryFilterRequest): IDbEntriesResponse =
        transactionWrapper({
            val res = entryTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.movieId != MovieId.NONE) {
                        add(entryTable.movieId eq rq.movieId.asString())
                    }
                    if (rq.searchString.isNotBlank()) {
                        add(entryTable.comment like "%${rq.searchString}%")
                    }
                    if (rq.viewingDateFrom != ViewingDate.NONE) {
                        add(entryTable.viewingDate greaterEq rq.viewingDateFrom.asLocalDate().toJavaLocalDate())
                    }
                    if (rq.viewingDateTo != ViewingDate.NONE) {
                        add(entryTable.viewingDate lessEq rq.viewingDateTo.asLocalDate().toJavaLocalDate())
                    }
                }.reduce { a, b -> a and b }
            }
            DbEntriesResponseOk(data = res.map { entryTable.from(it) })
        }, {
            DbEntriesResponseErr(it.asEntryError())
        })
}
