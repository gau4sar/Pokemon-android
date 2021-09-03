package com.gaurav.pokemon.data.remote

import com.gaurav.pokemon.data.remote.responses.GetCommunityResponse
import retrofit2.http.GET

interface PokeApiService {

    @GET("pokemon//")
    suspend fun getCommunityActivity(): GetCommunityResponse
}