package com.gaurav.pokemon.data.remote

import com.gaurav.pokemon.data.model.ApiTokenInfo

class FirebaseApiRemoteDataSource(private val apiService: FirebaseApiService) : BaseDataSource() {

    suspend fun getApiTokenInfo(): ResponseHandler<ApiTokenInfo> {
        return safeApiCall { apiService.getToken() }
    }

}