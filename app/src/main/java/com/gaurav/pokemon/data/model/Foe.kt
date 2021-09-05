package com.gaurav.pokemon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "foe")
data class Foe(
    @PrimaryKey
    val name: String,
    val pokemon: FoePokemon
): Serializable