package com.gaurav.pokemon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemonLocationInfo")
data class PokemonLocationInfo(
    @SerializedName("captured_at")
    val capturedAt: String,
    @SerializedName("captured_lat_at")
    val capturedLatAt: Double,
    @SerializedName("captured_long_at")
    val capturedLongAt: Double,
    @PrimaryKey
    val id: Int,
    val name: String
)