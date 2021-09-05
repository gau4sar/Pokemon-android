package com.gaurav.pokemon.data.remote.pokemon

import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.remote.BaseDataSource
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.remote.responses.GetPokemonListResponse

class PokemonApiRemoteDataSource(private val apiService: PokeApiService) : BaseDataSource() {

    suspend fun getPokemonInfoList(limit: String): ResponseHandler<GetPokemonListResponse> {
        return safeApiCall { apiService.getPokemonInfoList(limit) }
    }

    suspend fun getPokemonDetails(name: String): ResponseHandler<Pokemon> {
        return safeApiCall { apiService.getPokemonDetails(name) }
    }
}