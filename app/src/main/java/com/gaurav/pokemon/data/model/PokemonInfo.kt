package com.gaurav.pokemon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_list")
data class PokemonInfo(
    @PrimaryKey
    val name: String,
    val url: String
)