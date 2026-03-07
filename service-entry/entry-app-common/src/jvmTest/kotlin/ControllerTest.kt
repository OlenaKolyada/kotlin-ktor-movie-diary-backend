package com.funkycorgi.vulpecula.entry.app.common

import kotlinx.coroutines.test.runTest
import com.funkycorgi.vulpecula.entry.api.jvm.mappers.fromTransport
import com.funkycorgi.vulpecula.entry.api.jvm.mappers.toTransportEntry
import com.funkycorgi.vulpecula.entry.api.jvm.models.*
import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerTest {

    private val request = EntryCreateRequest(
        entry = EntryCreateObject(
            movieId = "movie:12345",
            viewingDate = "2025-10-15",
            rating = 9,
            comment = "Потрясающий фильм о надежде",
        ),
        debug = EntryDebug(mode = EntryRequestDebugMode.STUB, stub = EntryRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IEntryAppSettings = object : IEntryAppSettings {
        override val corSettings: EntryCorSettings = EntryCorSettings()
        override val processor: EntryProcessor = EntryProcessor(corSettings)
    }

    private suspend fun createEntry(request: EntryCreateRequest): EntryCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportEntry() as EntryCreateResponse },
        )

    @Test
    fun controllerHelperTest() = runTest {
        val res = createEntry(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
