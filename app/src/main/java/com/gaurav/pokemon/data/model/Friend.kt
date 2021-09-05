package com.gaurav.pokemon.data.model

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "friend")
data class Friend(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val pokemonCapturedInfo: PokemonCapturedInfo
) : Serializable