package com.gaurav.pokemon.ui.main.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.model.PokemonFound
import com.gaurav.pokemon.data.model.PokemonInfo
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.databinding.FragmentExploreBinding
import com.gaurav.pokemon.databinding.FragmentPokemonDetailsBinding
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.ui.main.screens.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.*
import com.gaurav.pokemon.utils.Constants.POKEMON_FOUND
import com.gaurav.pokemon.utils.GeneralUtils.generateRandomMarkers
import com.gaurav.pokemon.utils.GeneralUtils.pickPokemonsRandomly
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.java.KoinJavaComponent
import timber.log.Timber
import java.util.*


class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private lateinit var googleMap: GoogleMap

    lateinit var callback: OnMapReadyCallback

    var counter = 1

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().checkAndEnableGps()

        // Runtime Permission handler for Location access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (requireActivity() as BaseActivity).isLocationPermissionGranted()
        }

        viewModelWorks()
    }

    private fun viewModelWorks() {

        mainViewModel.pokemonInfoListAndCurrentLocationLiveData.observe(
            viewLifecycleOwner,
            {(pokemonInfoList, currentLocation) ->

                Timber.d("pokemonInfoListAndCurrentLocationLiveData ${pokemonInfoList} || ${currentLocation}")

                val minPokemon = MAX_POKEMONS - 4

                val totalPokemons = (minPokemon..MAX_POKEMONS).random()

                pickPokemonsRandomly(totalPokemons, pokemonInfoList) { randomPokemonList ->

                    callBack(
                        LatLng(currentLocation.latitude, currentLocation.longitude),
                        totalPokemons, randomPokemonList
                    )
                }
            })

       /* mainViewModel.fetchPokemonInfoList.observe(viewLifecycleOwner, { pokemonInfoList ->
            Timber.d("Saved pokemon info list : $pokemonInfoList")
            // TODO :: Use the info list here to set the item information on the pokemon balls

            val minPokemon = MAX_POKEMONS - 4

            val totalPokemons = (minPokemon..MAX_POKEMONS).random()

            pickPokemonsRandomly(totalPokemons, pokemonInfoList) { randomPokemonList ->

                mainViewModel.currentLocationLiveData.observe(
                    viewLifecycleOwner,
                    { currentLocation ->
                        callBack(
                            LatLng(currentLocation.latitude, currentLocation.longitude),
                            totalPokemons, randomPokemonList
                        )
                    })

                mainViewModel.moveToLocationLiveData.observe(
                    viewLifecycleOwner,
                    { moveCameraToLocation(it) })
            }
        })
*/
        mainViewModel.moveToLocationLiveData.observe(
            viewLifecycleOwner,
            { moveCameraToLocation(it) })
    }

    private fun callBack(
        latLng: LatLng,
        totalPokemons: Int,
        pokemonList: MutableList<PokemonInfo>
    ) {
        callback = OnMapReadyCallback { _googleMap ->

            googleMap = _googleMap

            googleMap.clear()
            val currentLocation = LatLng(latLng.latitude, latLng.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))

            clickListener()

            generateRandomMarkers(
                totalPokemons,
                mainViewModel.currentLocationLiveData.value!!,
                MIN_DISTANCE,
                MAX_DISTANCE
            ) {
                addMarker(it, pokemonList.last())
                pokemonList.removeLast()
            }

            moveCameraToLocation(currentLocation)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun clickListener() {

        googleMap.setOnMarkerClickListener { marker ->

            val intent = Intent(requireActivity(), PokemonDetailsActivity::class.java)
            val bundle = Bundle()
            when (counter) {
                1 -> {
                    bundle.putSerializable(
                        POKEMON_FOUND,
                        PokemonFound(1, false, marker.position, true)
                    )
                }
                2 -> {
                    bundle.putSerializable(
                        POKEMON_FOUND,
                        PokemonFound(1, true, marker.position, false)
                    )
                }
                else -> {
                    bundle.putSerializable(
                        POKEMON_FOUND,
                        PokemonFound(1, false, marker.position, false)
                    )
                    counter = 0
                }
            }
            counter++
            intent.putExtras(bundle)
            startActivity(intent)

            false
        }
    }

    private fun getCurrentLocation() {
        if (!requireActivity().isGpsEnabled()) {
            requireActivity().enableGps()
        } else {
            val location = mainViewModel.currentLocationLiveData.value
            if (location != null) {
                mainViewModel.moveToLocationLiveData.postValue(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                )
            } else {
                requireActivity().showToast(requireActivity().getString(R.string.unable_to_find_location))
                Timber.e("Unable to find location")
            }
        }
    }

    private fun addMarker(latLng: LatLng, pokemonInfo: PokemonInfo) {
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latLng.latitude, latLng.longitude))
                .title(pokemonInfo.name)
                .icon(
                    BitmapDescriptorFactory
                        .fromResource(R.drawable.icons8_pokeball_96)
                )
        )

        marker?.tag = pokemonInfo
    }

    private fun moveCameraToLocation(latLng: LatLng) {
        zoomin(LatLng(latLng.latitude, latLng.longitude))
    }

    private fun zoomout(latLng: LatLng) {
        animateTheCamera(latLng, 4f)
    }

    private fun zoomin(latLng: LatLng) {
        animateTheCamera(latLng, 12f)
    }

    private fun animateAndMoveTheCamera(latLng: LatLng) {
        animateTheCamera(latLng, 4f)
    }

    private fun animateTheCamera(latLng: LatLng, zoomLevel: Float) {
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(latLng.latitude, latLng.longitude)
            )
            .zoom(zoomLevel)
            .build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    companion object {
        private const val MAX_POKEMONS = 7

        //In meters
        private const val MIN_DISTANCE = 1000
        private const val MAX_DISTANCE = 3000
    }

}