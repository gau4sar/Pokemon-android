package com.gaurav.pokemon.data.remote.firebase

import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.remote.responses.GetCommunityResponse
import com.gaurav.pokemon.data.remote.responses.GetMyTeamResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface FirebaseApiService {

    @POST("/token?email=gaurav4sarma@gmail.com")
    suspend fun getToken() : ApiTokenInfo

    @GET("/activity")
    suspend fun getCommunityActivity(): GetCommunityResponse

    @GET("/my-team")
    suspend fun getMyTeam(): GetMyTeamResponse

}