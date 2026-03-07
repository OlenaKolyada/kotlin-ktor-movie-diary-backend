package com.funkycorgi.vulpecula.entry.app.common

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings

interface IEntryAppSettings {
    val processor: EntryProcessor
    val corSettings: EntryCorSettings
}
