package com.gaurav.pokemon.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gaurav.pokemon.data.model.ApiToken
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.repository.FirebaseApiRepository

class MainViewModel(private val firebaseApiRepository: FirebaseApiRepository) : ViewModel() {

    val apiTokenInfo: LiveData<ResponseHandler<ApiToken>> =
        firebaseApiRepository.observeApiTokenInfo

    val getApiTokenInfo = firebaseApiRepository.getApiTokenInfo
}