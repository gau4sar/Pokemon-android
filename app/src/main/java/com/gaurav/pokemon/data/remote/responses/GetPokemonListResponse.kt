package com.gaurav.pokemon.data.remote.responses

import com.gaurav.pokemon.data.model.PokemonInfo

data class GetPokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: ArrayList<PokemonInfo>
)