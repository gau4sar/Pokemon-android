package com.gaurav.pokemon.data.remote

import com.gaurav.pokemon.data.remote.responses.GetPokemonDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {

    @GET("/pokemon/{pokemonId}/")
    suspend fun getPokemonDetailsById(@Path(value = "pokemonId") pokemonId: String) : GetPokemonDetailsResponse
}