package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock

data class DbEntryIdRequest(
    val id: EntryId,
    val lock: EntryLock = EntryLock.NONE,
) {
    constructor(entry: Entry) : this(entry.id, entry.lock)
}
