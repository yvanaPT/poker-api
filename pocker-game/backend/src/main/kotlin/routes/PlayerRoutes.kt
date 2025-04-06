package com.pocker.routes

import com.pocker.data.database.*
import com.pocker.data.model.Player
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.playerRoutes() {

    get("/get-player-ranking") {
        val pokerGameId = call.queryParameters["pokerGameId"] ?: return@get call.respond(
            HttpStatusCode.OK,
            "Must provide poker game ID"
        )
        val playerRanking = getPlayerRanking(pokerGameId)
        if (playerRanking.isEmpty()) {
            call.respond(
                HttpStatusCode.OK,
                "No player found for this poker game ID"
            )
        } else call.respond(status = HttpStatusCode.OK, message = playerRanking.toTypedArray())
    }

    get("/get-player-info") {
        val pokerGameId = call.queryParameters["pokerGameId"]
        val playerId = call.queryParameters["playerId"]

        if (pokerGameId != null && playerId != null) {
            val playerInfo = getPlayerInfo(pokerGameId, playerId)
            if (playerInfo != null) {
                call.respond(HttpStatusCode.OK, playerInfo)
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    "No info found for this player"
                )
            }
        } else {
            call.respond(
                HttpStatusCode.BadRequest,
                "Must provide query parameters pokerGameId and playerId"
            )
        }
    }

    post("/add-player/{pokerGameId}") {
        try {
            val pokerGameId = call.pathParameters["pokerGameId"] ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                "Must provide an id"
            )
            val newPlayer = call.receive<Player>()

            if (addPlayer(pokerGameId, newPlayer)) {
                call.respond(HttpStatusCode.Created, newPlayer)
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Can't add player"
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                "Wrong Player format, please verify"
            )
        }
    }

    patch("/update-player/poker-game/player/score") {
        try {
            val pokerGameId = call.queryParameters["pokerGameId"]
            val playerId = call.queryParameters["playerId"]
            val score = call.receive<Int>()

            if (pokerGameId != null && playerId != null) {
                val result = updatePlayerScore(pokerGameId, playerId, score)
                when (result) {
                    1 -> call.respond(
                        HttpStatusCode.OK,
                        mapOf("message" to "updated successfully")
                    )

                    0 -> call.respond(
                        HttpStatusCode.OK,
                        mapOf("message" to "Score is the same as previous")
                    )

                    else -> call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("message" to "Can't update score")
                    )
                }
            } else {
                return@patch call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "Must provide ids")
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("message" to "Wrong format, please verify")
            )
        }
    }
}