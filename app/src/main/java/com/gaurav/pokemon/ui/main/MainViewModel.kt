package com.gaurav.pokemon.ui.main

import android.location.Location
import androidx.lifecycle.*
import com.gaurav.pokemon.data.model.*
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.repository.FirebaseApiRepository
import com.gaurav.pokemon.data.repository.PokemonApiRepository
import com.gaurav.pokemon.utils.EncryptPrefUtils
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val firebaseApiRepository: FirebaseApiRepository,
    private val pokemonApiRepository: PokemonApiRepository,
    private val encryptPrefs: EncryptPrefUtils
) : ViewModel() {

    init {
        fetchTokenInfoApi()
    }

    /**
     * Api token info handlers
     */

    val tokenInfoLiveData = firebaseApiRepository.fetchTokenInfo

    fun fetchTokenInfoApi() {

        Timber.d("fetchTokenInfo called !!!")

        viewModelScope.launch(Dispatchers.IO) {

            firebaseApiRepository.fetchTokenInfoApi().let { apiResponse ->
                when (apiResponse) {
                    is ResponseHandler.Success -> {

                        apiResponse.data?.let { getTokenInfoResponse ->

                            // Save token api to encrypted prefs
                            encryptPrefs.saveApiToken(getTokenInfoResponse.token)

                            firebaseApiRepository.saveTokenInfo(getTokenInfoResponse)
                        }
                    }

                    is ResponseHandler.Error -> {
                        Timber.e("Get token info error response: $apiResponse")
                        //handleApiError(apiResponse, requireActivity())
                    }

                    is ResponseHandler.Loading -> {
                    }
                }
            }
        }
    }

    /**
     * Live Location
     */

    var currentLocationLiveData: MutableLiveData<Location> = MutableLiveData()

    val moveToLocationLiveData: MutableLiveData<LatLng> = MutableLiveData()


    /**
     * Community info lists
     */
    val fetchFriendsList: LiveData<ResponseHandler<List<Friend>>> =
        firebaseApiRepository.observeCommunityActivity

    val fetchFoesList: LiveData<List<Foe>> =
        firebaseApiRepository.fetchFoesList

    /**
     * Pokemon info list
     */
    fun observePokemonInfoList(limit: String): LiveData<ResponseHandler<List<PokemonInfo>>> =
        pokemonApiRepository.observePokemonInfoList(limit)

    val fetchPokemonInfoList = pokemonApiRepository.fetchPokemonInfoList


    /**
     * Pokemon details
     */

    private val _pokemonDetailsLiveData = MutableLiveData<PokemonDetails>()
    val pokemonDetailsLiveData: LiveData<PokemonDetails> = _pokemonDetailsLiveData

    fun fetchPokemonDetails(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            pokemonApiRepository.fetchPokemonDetails(id).let { apiResponse ->
                when (apiResponse) {
                    is ResponseHandler.Success -> {

                        apiResponse.data?.let { getPokemonDetailsResponse ->
                            Timber.d("Pokemon details info ${getPokemonDetailsResponse}")
                            _pokemonDetailsLiveData.postValue(getPokemonDetailsResponse)
                        }
                    }

                    is ResponseHandler.Error -> {
                        Timber.e("Get token info error response: $apiResponse")
                        //handleApiError(apiResponse, requireActivity())
                    }

                    is ResponseHandler.Loading -> {
                    }
                }
            }
        }
    }

    /**
     * MyTeam info
     */

    val observeMyTeam = firebaseApiRepository.observeMyTeam

    val fetchMyTeamList = firebaseApiRepository.fetchMyTeamList

    val pokemonInfoListAndCurrentLocationLiveData: LiveData<Pair<List<PokemonInfo>, Location>> =
        object : MediatorLiveData<Pair<List<PokemonInfo>, Location>>() {
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
