package com.gaurav.pokemon.data.remote

import com.gaurav.pokemon.data.model.ApiTokenInfo
import retrofit2.http.POST

//TODO :: Integrate other api service and save the same locally on room db
interface FirebaseApiService {

    @POST("/token?email=gaurav4sarma@gmail.com")
    suspend fun getToken() : ApiTokenInfo

}