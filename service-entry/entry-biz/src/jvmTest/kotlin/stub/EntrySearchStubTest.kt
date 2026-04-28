package com.funkycorgi.vulpecula.entry.biz.stub

import com.funkycorgi.vulpecula.entry.biz.EntryProcessor
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryFilter
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EntrySearchStubTest {
    private val processor = EntryProcessor()
    private val filter = EntryFilter(searchString = "matrix")

    @Test
    fun search() = runTest {
        val ctx = context(EntryStubs.SUCCESS, filter)

        processor.exec(ctx)

        assertEquals(EntryState.FINISHING, ctx.state)
        assertTrue(ctx.entriesResponse.size > 1)
        assertTrue(ctx.entriesResponse.first().comment.contains(filter.searchString))
    }

    @Test
    fun badSearchString() = runTest {
        val ctx = context(EntryStubs.BAD_SEARCH_STRING, filter)

        processor.exec(ctx)

        assertEquals(Entry(), ctx.entryResponse)
        assertEquals("searchString", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = context(EntryStubs.DB_ERROR, filter)

        processor.exec(ctx)

        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    private fun context(stub: EntryStubs, filter: EntryFilter = EntryFilter()) = EntryContext(
        command = EntryCommand.SEARCH,
        state = EntryState.NONE,
        workMode = EntryWorkMode.STUB,
        stubCase = stub,
        entryFilterRequest = filter,
    )
}
