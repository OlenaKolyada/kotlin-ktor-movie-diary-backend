package com.funkycorgi.vulpecula.entry.app.common

import kotlinx.datetime.Clock
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.asEntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryState

suspend inline fun <T> IEntryAppSettings.controllerHelper(
    crossinline getRequest: suspend EntryContext.() -> Unit,
    crossinline toResponse: suspend EntryContext.() -> T,
): T {
    val ctx = EntryContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = EntryState.FAILING
        ctx.errors.add(e.asEntryError())
        processor.exec(ctx)
        if (ctx.command == EntryCommand.NONE) {
            ctx.command = EntryCommand.READ
        }
        ctx.toResponse()
    }
}
