package com.gaurav.pokemon.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "pokemonFriend",
    indices = [Index(value = ["friend_id_fk"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Friend::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("friend_id_fk"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FriendPokemon(
    @SerializedName("captured_at")
    val capturedAt: String,
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "friend_id_fk")
    val name: String
) : Serializable
