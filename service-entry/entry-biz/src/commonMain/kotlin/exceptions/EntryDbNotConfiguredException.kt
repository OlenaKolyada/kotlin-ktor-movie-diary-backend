package com.funkycorgi.vulpecula.entry.biz.exceptions

import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode

class EntryDbNotConfiguredException(
    workMode: EntryWorkMode,
) : RuntimeException("Database is not configured for work mode $workMode")
