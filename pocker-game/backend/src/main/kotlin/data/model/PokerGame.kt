package com.pocker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class PokerGame (
    @SerialName("_id")
    val id : String = ObjectId().toHexString(),
    val name : String,
    val players : MutableList<Player> = mutableListOf()
)