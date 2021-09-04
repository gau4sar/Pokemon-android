package com.gaurav.pokemon.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.model.Friend
import com.gaurav.pokemon.data.model.PokemonInfo
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.remote.responses.GetPokemonDetailsResponse
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

    val observeMyTeam = firebaseApiRepository.observeMyTeam

    val fetchMyTeamList = firebaseApiRepository.fetchMyTeamList

    val pokemonInfoListAndCurrentLocationLiveData: LiveData<Pair<List<PokemonInfo>, Location>> =
        object: MediatorLiveData<Pair<List<PokemonInfo>, Location>>() {
            var pokemonInfoList: List<PokemonInfo>? = null
            var location: Location? = null
            init {
                addSource(fetchPokemonInfoList) { pokemonInfoList ->
                    this.pokemonInfoList = pokemonInfoList
                    location?.let { value = pokemonInfoList to it }
                }
                addSource(currentLocationLiveData) { location ->
                    this.location = location
                    pokemonInfoList?.let { value = it to location }
                }
            }
        }
}
