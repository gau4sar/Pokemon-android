package com.gaurav.pokemon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "apiToken")
data class ApiToken(
    @PrimaryKey
    val token: String,
    val expiresAt: Double
) : Serializable