package com.funkycorgi.vulpecula.entry.biz.stub

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EntryDeleteStubTest {
    private val processor = EntryProcessor()
    private val id = EntryId("entry-888")

    @Test
    fun delete() = runTest {
        val ctx = context(EntryStubs.SUCCESS, Entry(id = id))

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertEquals(id, ctx.entryResponse.id)
    }

    @Test
    fun cannotDelete() = runTest {
        val ctx = context(EntryStubs.CANNOT_DELETE, Entry(id = id))

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("cannot-delete", ctx.errors.firstOrNull()?.code)
    }

    private fun context(stub: EntryStubs, entryRequest: Entry = Entry()) = EntryContext(
        command = EntryCommand.DELETE,
        state = EntryState.NONE,
        workMode = EntryWorkMode.STUB,
        stubCase = stub,
        entryRequest = entryRequest,
    )
}
