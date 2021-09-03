package com.gaurav.pokemon.data.remote.firebase

import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.remote.BaseDataSource
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.remote.responses.GetCommunityResponse

class FirebaseApiRemoteDataSource(private val apiService: FirebaseApiService) : BaseDataSource() {

    suspend fun getApiTokenInfo(): ResponseHandler<ApiTokenInfo> {
        return safeApiCall { apiService.getToken() }
    }

    suspend fun getCommunity(): ResponseHandler<GetCommunityResponse> {
        return safeApiCall { apiService.getCommunityActivity() }
    }

}