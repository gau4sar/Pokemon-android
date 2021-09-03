package com.gaurav.pokemon.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.model.Friend
import com.gaurav.pokemon.data.model.PokemonInfo
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.repository.FirebaseApiRepository
import com.gaurav.pokemon.data.repository.PokemonApiRepository
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val firebaseApiRepository: FirebaseApiRepository,
    private val pokemonApiRepository: PokemonApiRepository
) : ViewModel() {

    val apiTokenInfoLiveData: LiveData<ResponseHandler<ApiTokenInfo>> =
        firebaseApiRepository.observeApiTokenInfo

    val fetchTokenInfo = firebaseApiRepository.fetchTokenInfo

    var currentLocationLiveData: MutableLiveData<Location> = MutableLiveData()

    val moveToLocationLiveData: MutableLiveData<LatLng> = MutableLiveData()

    val fetchFriendsList: LiveData<ResponseHandler<List<Friend>>> =
        firebaseApiRepository.observeCommunityActivity

    fun observePokemonInfoList(limit: String): LiveData<ResponseHandler<List<PokemonInfo>>> =
        pokemonApiRepository.observePokemonInfoList(limit)

    val fetchPokemonInfoList = pokemonApiRepository.fetchPokemonInfoList

}
