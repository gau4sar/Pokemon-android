package com.gaurav.pokemon.data.remote.pokemon

import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.remote.responses.GetPokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonInfoList(@Query("limit") limit: String) : GetPokemonListResponse

    @GET("pokemon/{pokemonName}/")
    suspend fun getPokemonDetails(@Path(value = "pokemonName") pokemonName: String) : Pokemon
}