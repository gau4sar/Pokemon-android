package com.gaurav.pokemon.data.remote

import com.gaurav.pokemon.data.model.ApiToken

class FirebaseApiRemoteDataSource(private val apiService: FirebaseApiService) : BaseDataSource() {

    suspend fun getToken(): ResponseHandler<ApiToken> {
        return safeApiCall { apiService.getToken() }
    }

}