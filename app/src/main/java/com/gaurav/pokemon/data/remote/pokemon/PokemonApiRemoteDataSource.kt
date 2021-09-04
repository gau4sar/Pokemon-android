package com.gaurav.pokemon.data.remote.pokemon

import com.gaurav.pokemon.data.remote.BaseDataSource
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.remote.responses.GetPokemonDetailsResponse
import com.gaurav.pokemon.data.remote.responses.GetPokemonListResponse

class PokemonApiRemoteDataSource(private val apiService: PokeApiService) : BaseDataSource() {

    suspend fun getPokemonInfoList(limit: String): ResponseHandler<GetPokemonListResponse> {
        return safeApiCall { apiService.getPokemonInfoList(limit) }
    }

    suspend fun getPokemonDetails(id: Int): ResponseHandler<GetPokemonDetailsResponse> {
        return safeApiCall { apiService.getPokemonDetailsById(id.toString()) }
    }
}