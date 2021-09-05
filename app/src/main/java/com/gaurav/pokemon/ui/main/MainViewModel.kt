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

    /**
     * Api token info handlers
     */

    val tokenInfoLiveData: LiveData<ApiTokenInfo> = firebaseApiRepository.fetchTokenInfo

    fun fetchTokenInfoApi() {

        viewModelScope.launch(Dispatchers.IO) {

            firebaseApiRepository.fetchTokenInfoApi().let { apiResponse ->
                when (apiResponse) {

                    is ResponseHandler.Success -> {
                        apiResponse.data?.let { getTokenInfoResponse ->
                            Timber.d("fetchTokenInfo called $getTokenInfoResponse")

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
    fun observePokemonInfoList(limit: String): LiveData<ResponseHandler<List<PokemonList>>> =
        pokemonApiRepository.observePokemonInfoList(limit)

    val fetchPokemonInfoList = pokemonApiRepository.fetchPokemonInfoList


    /**
     * Pokemon details
     */

    private val _pokemonLiveData = MutableLiveData<Pokemon>()
    val pokemonLiveData: LiveData<Pokemon> = _pokemonLiveData

    fun fetchPokemonDetails(name: String) {

        viewModelScope.launch(Dispatchers.IO) {
            pokemonApiRepository.fetchPokemonDetails(name).let { apiResponse ->
                when (apiResponse) {
                    is ResponseHandler.Success -> {

                        apiResponse.data?.let { getPokemonResponse ->
                            Timber.d("Pokemon details info ${getPokemonResponse}")
                            _pokemonLiveData.postValue(getPokemonResponse)
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

    val pokemonListListAndCurrentLocationLiveData: LiveData<Pair<List<PokemonList>, Location>> =
        object : MediatorLiveData<Pair<List<PokemonList>, Location>>() {
            var pokemonListList: List<PokemonList>? = null
            var location: Location? = null

            init {
                addSource(fetchPokemonInfoList) { pokemonInfoList ->
                    this.pokemonListList = pokemonInfoList
                    location?.let { value = pokemonInfoList to it }
                }
                addSource(currentLocationLiveData) { location ->
                    this.location = location
                    pokemonListList?.let { value = it to location }
                }
            }
        }

    /**
     * Captured info
     */

    private val _capturedListLiveData = MutableLiveData<List<PokemonLocationInfo>>()
    val capturedListLiveData: LiveData<List<PokemonLocationInfo>> = _capturedListLiveData

    val observeCapturedList = firebaseApiRepository.observeCapturedInfo

    fun fetchCapturedList() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApiRepository.fetchCapturedList().let { apiResponse ->
                when (apiResponse) {

                    is ResponseHandler.Success -> {

                        apiResponse.data?.let { getCapturedListResponse ->
                            Timber.d("Pokemon details info $getCapturedListResponse")
                            _capturedListLiveData.postValue(getCapturedListResponse)
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
}
