package com.gaurav.pokemon.data.remote.pokemon

import com.gaurav.pokemon.data.remote.BaseDataSource
import com.gaurav.pokemon.data.remote.PokeApiService
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.remote.responses.GetPokemonDetailsResponse

class PokemonApiRemoteDataSource(private val apiService: PokeApiService) : BaseDataSource() {

    suspend fun getPokemonDetails(id: String): ResponseHandler<GetPokemonDetailsResponse> {
        return safeApiCall { apiService.getPokemonDetailsById(id) }
    }
}