package com.funkycorgi.vulpecula.entry.repo.inmemory

import com.benasher44.uuid.uuid4
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.repo.*
import com.funkycorgi.vulpecula.entry.common.repo.exceptions.RepoEmptyLockException
import com.funkycorgi.vulpecula.entry.repo.common.IRepoEntryInitializable
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class EntryRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : EntryRepoBase(), IRepoEntry, IRepoEntryInitializable {
    private val mutex = Mutex()
    private val cache = Cache.Builder<String, EntryEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(entries: Collection<Entry>) = entries.map { entry ->
        val entity = EntryEntity(entry)
        require(entity.id != null)
        cache.put(entity.id, entity)
        entry
    }

    override suspend fun createEntry(request: DbEntryRequest): IDbEntryResponse = tryEntryMethod {
        val key = randomUuid()
        val entry = request.entry.copy(id = EntryId(key), lock = EntryLock(randomUuid()))
        val entity = EntryEntity(entry)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbEntryResponseOk(entry)
    }

    override suspend fun readEntry(request: DbEntryIdRequest): IDbEntryResponse = tryEntryMethod {
        val key = request.id.takeIf { it != EntryId.NONE }?.asString() ?: return@tryEntryMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let { DbEntryResponseOk(it.toInternal()) }
                ?: errorNotFound(request.id)
        }
    }

    override suspend fun updateEntry(request: DbEntryRequest): IDbEntryResponse = tryEntryMethod {
        val requestEntry = request.entry
        val id = requestEntry.id.takeIf { it != EntryId.NONE } ?: return@tryEntryMethod errorEmptyId
        val key = id.asString()
        val oldLock = requestEntry.lock.takeIf { it != EntryLock.NONE } ?: return@tryEntryMethod errorEmptyLock(id)

        mutex.withLock {
            val oldEntry = cache.get(key)?.toInternal()
            when {
                oldEntry == null -> errorNotFound(id)
                oldEntry.lock == EntryLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldEntry.lock != oldLock -> errorRepoConcurrency(oldEntry, oldLock)
                else -> {
                    val newEntry = requestEntry.copy(lock = EntryLock(randomUuid()))
                    cache.put(key, EntryEntity(newEntry))
                    DbEntryResponseOk(newEntry)
                }
            }
        }
    }

    override suspend fun deleteEntry(request: DbEntryIdRequest): IDbEntryResponse = tryEntryMethod {
        val id = request.id.takeIf { it != EntryId.NONE } ?: return@tryEntryMethod errorEmptyId
        val key = id.asString()
        val oldLock = request.lock.takeIf { it != EntryLock.NONE } ?: return@tryEntryMethod errorEmptyLock(id)

        mutex.withLock {
            val oldEntry = cache.get(key)?.toInternal()
            when {
                oldEntry == null -> errorNotFound(id)
                oldEntry.lock == EntryLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldEntry.lock != oldLock -> errorRepoConcurrency(oldEntry, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbEntryResponseOk(oldEntry)
                }
            }
        }
    }

    override suspend fun searchEntry(request: DbEntryFilterRequest): IDbEntriesResponse = tryEntriesMethod {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                request.movieId.takeIf { it != MovieId.NONE }?.let {
                    it.asString() == entry.value.movieId
                } ?: true
            }
            .filter { entry ->
                request.viewingDateFrom.takeIf { it != ViewingDate.NONE }?.let {
                    val viewingDate = entry.value.viewingDate
                    viewingDate != null && viewingDate >= it.asLocalDate()
                } ?: true
            }
            .filter { entry ->
                request.viewingDateTo.takeIf { it != ViewingDate.NONE }?.let {
                    val viewingDate = entry.value.viewingDate
                    viewingDate != null && viewingDate <= it.asLocalDate()
                } ?: true
            }
            .filter { entry ->
                request.searchString.takeIf { it.isNotBlank() }?.let {
                    entry.value.comment?.contains(it, ignoreCase = true) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbEntriesResponseOk(result)
    }
}
