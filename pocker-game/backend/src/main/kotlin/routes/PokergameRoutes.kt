package com.pocker.routes

import com.pocker.data.database.createPokerGame
import com.pocker.data.database.deletePokerGame
import com.pocker.data.database.getPokerGame
import com.pocker.data.database.getPokerGames
import com.pocker.data.model.PokerGame
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.pokerGameRoutes() {

    post("/create-poker-game") {
        try {
            val newPokerGame = call.receive<PokerGame>()
            if (createPokerGame(newPokerGame)) {
                call.respond(HttpStatusCode.Created, newPokerGame)
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Can't create new poker game"
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                "Wrong Poker Game format, please verify"
            )
        }
    }

    get("/get-poker-game") {
        val pokerGameList = getPokerGames()
        val id = call.queryParameters["id"] ?: return@get call.respond(HttpStatusCode.OK, pokerGameList)
        val pokerGame = getPokerGame(id)
        if (pokerGame != null) {
            call.respond(HttpStatusCode.OK, pokerGame)
        } else {
            call.respond(
                HttpStatusCode.BadRequest,
                "No poker Game found with matching ID"
            )
        }
    }

    delete("/delete-poker-game/{id}") {
        val pokerGameId = call.pathParameters["id"] ?: return@delete call.respond(
            HttpStatusCode.BadRequest,
            "Must provide an id"
        )
        if (deletePokerGame(pokerGameId)) {
            call.respond(HttpStatusCode.OK, "Deleted Successfully")
        } else {
            call.respond(HttpStatusCode.OK, "Error, can't delete poker Game")
        }
    }
}