package com.funkycorgi.vulpecula.entry.app.ktor

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.entryRoutes(appSettings: EntryAppSettings) {
    post("create") {
        call.createEntry(appSettings)
    }
    post("read") {
        call.readEntry(appSettings)
    }
    post("update") {
        call.updateEntry(appSettings)
    }
    post("delete") {
        call.deleteEntry(appSettings)
    }
    post("search") {
        call.searchEntry(appSettings)
    }
}
