package com.gaurav.pokemon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foe")
data class Foe(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val pokemon: Pokemon
)