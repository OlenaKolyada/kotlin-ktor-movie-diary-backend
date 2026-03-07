package com.funkycorgi.vulpecula.entry.app.ktor

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import com.funkycorgi.vulpecula.entry.api.jvm.entryApiJvmSerializer
import com.funkycorgi.vulpecula.entry.app.ktor.plugins.initAppSettings

@Suppress("unused")
fun Application.module(
    appSettings: EntryAppSettings = initAppSettings(),
) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }

    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, "OK")
        }
        route("entry") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(entryApiJvmSerializer.serializationConfig)
                    setConfig(entryApiJvmSerializer.deserializationConfig)
                }
            }
            entryRoutes(appSettings)
        }
    }
}
