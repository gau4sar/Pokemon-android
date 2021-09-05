package com.gaurav.pokemon.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "pokemonFoe",
    indices = [Index(value = ["foe_id_fk"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Foe::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("foe_id_fk"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoePokemon(
    @SerializedName("captured_at")
    val capturedAt: String,
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "foe_id_fk")
    val name: String
) : Serializable