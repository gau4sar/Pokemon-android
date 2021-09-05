package com.gaurav.pokemon.data.model

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "friend")
data class Friend(
    @PrimaryKey
    val name: String,
    val pokemon: FriendPokemon
) : Serializable