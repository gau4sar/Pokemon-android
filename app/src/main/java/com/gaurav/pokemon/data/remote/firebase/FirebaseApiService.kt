package com.gaurav.pokemon.data.remote.firebase

import com.gaurav.pokemon.data.model.*
import com.gaurav.pokemon.data.remote.responses.CaptureResponse
import com.gaurav.pokemon.data.remote.responses.FriendsAndFoes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FirebaseApiService {

    @POST("/token?email=gaurav4sarma@gmail.com")
    suspend fun getToken(): ApiTokenInfo

    @GET("/activity")
    suspend fun getCommunityActivity(): FriendsAndFoes

    @GET("/my-team")
    suspend fun getMyTeam(): List<MyTeam>

    @GET("/captured")
    suspend fun getCaptured(): List<PokemonLocationInfo>

    @POST("/capture")
    suspend fun postCaptured(@Body capture: CapturePokemon): CaptureResponse

}