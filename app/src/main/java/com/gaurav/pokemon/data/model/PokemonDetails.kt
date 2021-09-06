package com.gaurav.pokemon.data.model

import java.io.Serializable

data class PokemonDetails(
    val pokemonLocationInfo: PokemonLocationInfo,
    val pokemonStatus: Int,
    val capturedByName: String
) : Serializable
