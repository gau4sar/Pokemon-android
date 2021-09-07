package com.gaurav.pokemon.ui.main

import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.model.CapturePokemon
import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.model.PokemonList
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.data.remote.responses.FriendsAndFoes
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
    val fetchCommunityActivity: LiveData<ResponseHandler<FriendsAndFoes>> =
        firebaseApiRepository.observeCommunityActivity

    /**
     * Pokemon info list
     */
    fun observePokemonInfoList(limit: String): LiveData<ResponseHandler<List<PokemonList>>> =
        pokemonApiRepository.observePokemonInfoList(limit)

    val fetchPokemonInfoList = pokemonApiRepository.fetchPokemonInfoList

    val pokemonListAndCurrentLocationLiveData: LiveData<Pair<List<PokemonList>, Location>> =
        object : MediatorLiveData<Pair<List<PokemonList>, Location>>() {
            var pokemonList: List<PokemonList>? = null
            var location: Location? = null

            init {
                addSource(fetchPokemonInfoList) { pokemonInfoList ->
                    this.pokemonList = pokemonInfoList
                    location?.let { value = pokemonInfoList to it }
                }
                addSource(currentLocationLiveData) { location ->
                    this.location = location
                    pokemonList?.let { value = it to location }
                }
            }
        }


    /**
     * Pokemon details
     */

    suspend fun fetchPokemonDetails (id:String) : ResponseHandler<Pokemon> =
        pokemonApiRepository.fetchPokemonDetails(id)

    /**
     * MyTeam info
     */

    val observeMyTeam = firebaseApiRepository.observeMyTeam

    val fetchMyTeamList = firebaseApiRepository.fetchMyTeamList

    /**
     * Captured info
     */
    val observeCapturedList = firebaseApiRepository.observeCapturedInfo

    /**
     * Post capture pokemon
     */
    suspend fun capturePokemonData(capturePokemon: CapturePokemon) =
        firebaseApiRepository.postCapturePokemon(capturePokemon)

    /**
     * Calculate dominant color
     */
    fun calcDominantColor(bitmap: Bitmap, onFinish: (Int) -> Unit) {

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish((colorValue))
            }
        }
    }

}
