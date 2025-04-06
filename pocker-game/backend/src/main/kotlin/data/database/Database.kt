package com.pocker.data.database

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.pocker.data.model.Player
import com.pocker.data.model.PokerGame
import com.pocker.data.model.RankedPlayer
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

val client = MongoClient.create("mongodb://localhost:27017")
val db = client.getDatabase("poker_db")
val pokerGameCollection = db.getCollection<PokerGame>("poker_game")

suspend fun createPokerGame(pokerGame: PokerGame): Boolean {
    return try {
        pokerGameCollection.insertOne(pokerGame).wasAcknowledged()
    } catch (e: Exception) {
        println("Something went wrong! ${e.message}")
        false
    }
}

suspend fun getPokerGame(pokerGameId: String): PokerGame? {
    return try {
        val filter = Filters.eq("_id", pokerGameId)
        pokerGameCollection.find(filter).firstOrNull()
    } catch (e : Exception){
        println("Something went wrong while finding poker game! ${e.message}")
        null
    }
}

suspend fun getPokerGames(): List<PokerGame> {
    return try {
        pokerGameCollection.find().toList()
    }  catch (e : Exception){
        println("Something went wrong while fetching poker game! ${e.message}")
        emptyList()
    }
}

suspend fun deletePokerGame(pokerGameId: String): Boolean {
    return try {
        val filter = Filters.eq("_id", pokerGameId)
        pokerGameCollection.deleteOne(filter).deletedCount == 1L
    } catch (e : Exception){
        println("Something went wrong while deleting poker game! ${e.message}")
        false
    }
}

suspend fun addPlayer(pokerGameId: String, player: Player): Boolean {
    return try {
        val filter = Filters.eq("_id", pokerGameId)
        val pokerGame = getPokerGame(pokerGameId)
        pokerGame?.players?.add(player)
        pokerGame?.let { pokerGameCollection.replaceOne(filter, it).modifiedCount } == 1L
    } catch (e: Exception) {
        println("Something went wrong while adding player! ${e.message}")
        false
    }
}

suspend fun getPlayerInfo(pokerGameId: String, playerId: String): RankedPlayer? {
    val playerRanking = getPlayerRanking(pokerGameId)
    return playerRanking.find { it.player.id == playerId }
}

suspend fun getPlayerRanking(pokerGameId: String): List<RankedPlayer> {
    val pokerGame = getPokerGame(pokerGameId)
    val playerRanking = pokerGame?.players?.sortedByDescending { it.score } ?: emptyList()
    return playerRanking.withIndex().associate { it.value to it.index + 1 }.map { (player, rank) ->
        RankedPlayer(rank, player)
    }
}

suspend fun updatePlayerScore(pokerGameId: String, playerId: String, score: Int): Int {
    try {
        val filter = Filters.eq("_id", pokerGameId)
        val pokerGame = getPokerGame(pokerGameId)
        pokerGame?.players?.find { it.id == playerId }?.let {
            it.score = score
        }
        val result =  pokerGame?.let { pokerGameCollection.replaceOne(filter, it)}

        return when (result?.modifiedCount) {
            1L -> 1
            0L -> return 0
            else -> -1
        }
    } catch (e : Exception){
        return -1
    }
}