package com.gaurav.pokemon.data.remote

import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.remote.responses.GetCommunityResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface FirebaseApiService {

    @POST("/token?email=gaurav4sarma@gmail.com")
    suspend fun getToken() : ApiTokenInfo

    @GET("/activity")
    suspend fun getCommunityActivity(): GetCommunityResponse

}