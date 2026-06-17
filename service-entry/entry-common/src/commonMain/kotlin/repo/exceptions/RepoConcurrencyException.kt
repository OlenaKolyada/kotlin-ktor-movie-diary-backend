package com.funkycorgi.vulpecula.entry.common.repo.exceptions

import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock

class RepoConcurrencyException(
    id: EntryId,
    expectedLock: EntryLock,
    actualLock: EntryLock?,
) : RepoEntryException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock",
)
