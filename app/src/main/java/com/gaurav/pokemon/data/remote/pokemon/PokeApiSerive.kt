package com.gaurav.pokemon.data.remote.pokemon

import com.gaurav.pokemon.data.remote.responses.GetPokemonDetailsResponse
import com.gaurav.pokemon.data.remote.responses.GetPokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonInfoList(@Query("limit") limit: String) : GetPokemonListResponse

    @GET("pokemon/{pokemonId}/")
    suspend fun getPokemonDetailsById(@Path(value = "pokemonId") pokemonId: String) : GetPokemonDetailsResponse
}