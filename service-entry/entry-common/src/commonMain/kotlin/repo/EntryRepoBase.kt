package com.funkycorgi.vulpecula.entry.common.repo

import com.funkycorgi.vulpecula.entry.common.helpers.errorSystem

abstract class EntryRepoBase : IRepoEntry {
    protected suspend fun tryEntryMethod(block: suspend () -> IDbEntryResponse) = try {
        block()
    } catch (e: Throwable) {
        DbEntryResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryEntriesMethod(block: suspend () -> IDbEntriesResponse) = try {
        block()
    } catch (e: Throwable) {
        DbEntriesResponseErr(errorSystem("methodException", e = e))
    }
}
