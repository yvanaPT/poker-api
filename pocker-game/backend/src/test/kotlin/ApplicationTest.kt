package com.pocker

import com.pocker.data.model.Player
import com.pocker.data.model.PokerGame
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testGetPokerGameWithNoId() = testApplication {
        application {
            module()
        }
        client.get("/get-poker-game").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testCreatePokerGameOK() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val id = Random.nextInt().toString()

        val response = client.post("/create-poker-game") {
            contentType(ContentType.Application.Json)
            setBody(PokerGame(id, "TestGame"))
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testCreatePokerGameNotOk() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/create-poker-game") {
            contentType(ContentType.Application.Json)
            setBody("")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testDeletePokerGameWithNoId() = testApplication {
        application {
            module()
        }
        client.delete("/delete-poker-game").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun testDeletePokerGameWithWrongId() = testApplication {
        application {
            module()
        }
        client.delete("/delete-poker-game/dumb").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "Error, can't delete poker Game",
                bodyAsText()
            )
        }
    }

    @Test
    fun testGetPlayerRankingWithNoId() = testApplication {
        application {
            module()
        }
        client.get("/get-player-ranking").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "Must provide poker game ID",
                bodyAsText()
            )
        }
    }

    @Test
    fun testGetPlayerRankingWithWrongId() = testApplication {
        application {
            module()
        }
        client.get("/get-player-ranking?pokerGameId=dumb").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "No player found for this poker game ID",
                bodyAsText()
            )
        }
    }

    @Test
    fun testGetPlayerRankingOk() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val pokerGameId = Random.nextInt().toString()
        val pokerGame = PokerGame(
            pokerGameId,
            "TestGame",
            mutableListOf(Player(username = "PlayerTest1", score = 10), Player(username = "PlayerTest2", score = 15))
        )
        client.post("/create-poker-game") {
            contentType(ContentType.Application.Json)
            setBody(pokerGame)
        }

        val response = client.get("/get-player-ranking?pokerGameId=$pokerGameId")
        val responseBodyJson = JSONArray(response.bodyAsText())
        val firstPlayer: JSONObject = responseBodyJson.getJSONObject(0).get("player") as JSONObject
        val firstPlayerName = firstPlayer.get("username") as String
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(firstPlayerName, "PlayerTest2")
    }

    @Test
    fun testUpdatePlayerScoreOk() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val pokerGameId = Random.nextInt().toString()
        val pokerGame = PokerGame(
            pokerGameId,
            "TestGame1",
            mutableListOf(Player(id = "13", username = "PlayerTest1"))
        )
        client.post("/create-poker-game") {
            contentType(ContentType.Application.Json)
            setBody(pokerGame)
        }

        val response = client.patch("/update-player/poker-game/player/score?pokerGameId=$pokerGameId&playerId=13") {
            contentType(ContentType.Application.Json)
            setBody(40)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "successfully")
    }

    @Test
    fun testUpdatePlayerScoreNotOK() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val pokerGameId = Random.nextInt().toString()
        val pokerGame = PokerGame(
            pokerGameId,
            "TestGame1",
            mutableListOf(Player(id = "13", username = "PlayerTest1"))
        )
        client.post("/create-poker-game") {
            contentType(ContentType.Application.Json)
            setBody(pokerGame)
        }

        val response = client.patch("/update-player/poker-game/player/score?pokerGameId=$pokerGameId&playerId=13") {
            contentType(ContentType.Application.Json)
            setBody("")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "Wrong")
    }

    @Test
    fun testGetPlayerInfoOk() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val pokerGameId = Random.nextInt().toString()
        val pokerGame = PokerGame(
            pokerGameId,
            "TestGame1",
            mutableListOf(Player(id = "13", username = "PlayerTest1"))
        )
        client.post("/create-poker-game") {
            contentType(ContentType.Application.Json)
            setBody(pokerGame)
        }

        val response = client.get("/get-player-info?pokerGameId=$pokerGameId&playerId=13")

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "PlayerTest1")
    }

    @Test
    fun testGetPlayerInfoWithWrongId() = testApplication {
        application {
            module()
        }

        val response = client.get("/get-player-info?pokerGameId=dumb&playerId=13")

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "No info")
    }

    @Test
    fun testAddPlayerOk() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val pokerGameId = Random.nextInt().toString()
        val pokerGame = PokerGame(
            pokerGameId,
            "TestGame1"
        )
        val player = Player("13", "PlayerTest3")
        client.post("/create-poker-game") {
            contentType(ContentType.Application.Json)
            setBody(pokerGame)
        }

        val response = client.post("/add-player/$pokerGameId") {
            contentType(ContentType.Application.Json)
            setBody(player)
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertContains(response.bodyAsText(), "PlayerTest3")
    }

    @Test
    fun testAddPlayerNotOk() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val player = Player("13", "PlayerTest3")

        val response = client.post("/add-player/dumb") {
            contentType(ContentType.Application.Json)
            setBody(player)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "Can't add player")
    }

}
