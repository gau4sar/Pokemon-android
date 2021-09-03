package com.gaurav.pokemon.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "pokemon", foreignKeys = [
        ForeignKey(
            entity = Friend::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("friend_id_fk"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Pokemon(
    @SerializedName("captured_at")
    val capturedAt: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "friend_id_fk", index = true)
    val friendIdFk: Int
) : Serializable