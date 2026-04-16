package com.funkycorgi.vulpecula.entry.app.ktor

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.funkycorgi.vulpecula.entry.api.jvm.mappers.fromTransport
import com.funkycorgi.vulpecula.entry.api.jvm.mappers.toTransportEntry
import com.funkycorgi.vulpecula.entry.api.jvm.models.IRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.IResponse
import com.funkycorgi.vulpecula.entry.app.common.controllerHelper

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processEntry(
    appSettings: EntryAppSettings,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    { respond(toTransportEntry()) },
)
