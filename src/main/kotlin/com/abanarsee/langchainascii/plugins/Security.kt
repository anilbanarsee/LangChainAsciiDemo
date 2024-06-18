package com.abanarsee.shared.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun Application.configureSecurity() {
    val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}"}

    val hashUserTable = UserHashedTableAuth(
        table = mapOf(
            "moth" to digestFunction("lantern"),
            "lantern" to digestFunction("forge"),
            "forge" to digestFunction("edge"),
            "edge" to digestFunction("winter"),
            "winter" to digestFunction("heart"),
            "heart" to digestFunction("grail"),
            "grail" to digestFunction("moth"),
            "knock" to digestFunction("knock")
        ),
        digester = digestFunction
    )

    authentication {
        basic(name = "myauth1") {
            realm = "Ktor Server"
            validate { credentials ->
                hashUserTable.authenticate(credentials)
            }
        }
    }
    routing {
        authenticate("myauth1") {
            get("/protected/route/basic") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
    }
}
