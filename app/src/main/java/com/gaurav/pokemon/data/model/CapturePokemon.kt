package com.gaurav.pokemon.data.model

data class CapturePokemon(
    val pokemon: PokemonCapture
)

data class PokemonCapture(
    val id: Int,
    val name: String,
    val lat: Long,
    val long: Long
)
