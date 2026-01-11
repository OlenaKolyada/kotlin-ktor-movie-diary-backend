package com.funkycorgi.vulpecula.entry.stubs

import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.Comment
import com.funkycorgi.vulpecula.entry.stubs.EntryStubObjects.*

object EntryStub {

    fun get(): Entry = ENTRY_SUCCESS.copy()

    fun prepareResult(
        block: Entry.() -> Unit
    ): Entry = get()
        .apply(block)

    fun prepareSearchList(
        filter: String
    ) = listOf(
        createEntry("entry-01", filter),
        createEntry("entry-02", filter),
        createEntry("entry-03", filter),
        createEntry("entry-04", filter),
        createEntry("entry-05", filter),
        createEntry("entry-06", filter),
    )

    private fun createEntry(
        id: String,
        filter: String
    ) =
        entry(
            ENTRY_SUCCESS,
            id = id,
            filter = filter
        )

    private fun entry(
        base: Entry,
        id: String,
        filter: String
    ) = base.copy(
        id = EntryId(id),
        comment = Comment("$filter $id")
    )

}
