package com.funkycorgi.vulpecula.entry.common.repo.exceptions

import com.funkycorgi.vulpecula.entry.common.models.EntryId

class RepoEmptyLockException(id: EntryId) : RepoEntryException(
    id,
    "Lock is empty in DB",
)
