package com.pocker

import com.pocker.routes.playerRoutes
import com.pocker.routes.pokerGameRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        get("/") {
            call.respondText("Welcome to poker Game API!")
        }

        pokerGameRoutes()
        playerRoutes()

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}
