package com.funkycorgi.vulpecula.entry.biz.stub

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.stubs.EntryStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EntryReadStubTest {
    private val processor = EntryProcessor()
    private val id = EntryId("entry-666")

    @Test
    fun read() = runTest {
        val ctx = context(EntryStubs.SUCCESS, Entry(id = id))

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertEquals(id, ctx.entryResponse.id)
        assertEquals(EntryStub.get().movieId, ctx.entryResponse.movieId)
    }

    @Test
    fun badId() = runTest {
        val ctx = context(EntryStubs.BAD_ID)

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = context(EntryStubs.DB_ERROR)

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    private fun context(stub: EntryStubs, entryRequest: Entry = Entry()) = EntryContext(
        command = EntryCommand.READ,
        state = EntryState.NONE,
        workMode = EntryWorkMode.STUB,
        stubCase = stub,
        entryRequest = entryRequest,
    )
}
