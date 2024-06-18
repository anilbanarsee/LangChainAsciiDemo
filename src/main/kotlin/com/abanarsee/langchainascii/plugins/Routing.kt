package com.abanarsee.langchainascii.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.abanarsee.langchainascii.controller.tristanController

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        tristanController()
    }
}
