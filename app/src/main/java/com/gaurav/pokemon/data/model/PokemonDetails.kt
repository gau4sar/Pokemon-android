package com.gaurav.pokemon.data.model

import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gaurav.pokemon.data.model.pokemon.*
import com.google.gson.annotations.SerializedName

data class PokemonDetails(

    val abilities: List<Ability>,

    @SerializedName("base_experience")
    val baseExperience: Int,

    val forms: List<Form>,

    @SerializedName("game_indices")
    val gameIndices: List<GameIndice>,

    val height: Int,

    @Ignore
    @SerializedName("held_items")
    val heldItems: List<Any>,

    @PrimaryKey
    val id: Int,

    @SerializedName("is_default")
    val isDefault: Boolean,

    @SerializedName("location_area_encounters")
    val locationAreaEncounters: String,

    val moves: List<Move>,
    val name: String,
    val order: Int,

    @Ignore
    @SerializedName("past_types")
    val pastTypes: List<Any>,

    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int

)