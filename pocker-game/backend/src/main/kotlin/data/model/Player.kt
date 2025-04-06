package com.pocker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Player (
    @SerialName("_id")
    val id : String = ObjectId().toHexString(),
    val username : String,
    var score : Int = 0
)

@Serializable
data class RankedPlayer(val rank : Int, val player : Player)