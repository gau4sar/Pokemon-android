package com.gaurav.pokemon.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.repository.FirebaseApiRepository
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val firebaseApiRepository: FirebaseApiRepository
) : ViewModel() {

    val apiTokenInfoLiveData: LiveData<ResponseHandler<ApiTokenInfo>> =
        firebaseApiRepository.observeApiTokenInfo

    val fetchTokenInfo = firebaseApiRepository.fetchTokenInfo

    var mCurrentLocation: MutableLiveData<Location> = MutableLiveData()

    val moveToLocation: MutableLiveData<LatLng> = MutableLiveData()

}
