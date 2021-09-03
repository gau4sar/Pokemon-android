package com.gaurav.pokemon.ui.main.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.model.PokemonFound
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.ui.main.screens.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.*
import com.gaurav.pokemon.utils.Constants.POKEMON_FOUND
import com.gaurav.pokemon.utils.GeneralUtils.generateRandomMarkers
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.util.*


class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private lateinit var googleMap: GoogleMap

    lateinit var callback: OnMapReadyCallback

    var counter = 1

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
        mainViewModel.currentLocationLiveData.observe(viewLifecycleOwner, { currentLocation ->
            callBack(LatLng(currentLocation.latitude,currentLocation.longitude))
        })

        mainViewModel.moveToLocationLiveData.observe(
            viewLifecycleOwner,
            { moveCameraToLocation(it) })

        mainViewModel.fetchPokemonInfoList.observe(viewLifecycleOwner, {
            Timber.d("Saved pokemon info list : $it")
            // TODO :: Use the info list here to set the item information on the pokemon balls
        })
    }

    private fun callBack(latLng: LatLng) {
        callback = OnMapReadyCallback { _googleMap ->

            val currentLocation = LatLng(latLng.latitude, latLng.longitude)
            _googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            googleMap = _googleMap

            clickListener()

            generateRandomMarkers(TOTAL_POKEMONS, mainViewModel.currentLocationLiveData.value!!,MIN_DISTANCE,MAX_DISTANCE) {
                addMarker(it)
            }

            moveCameraToLocation(currentLocation)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private fun clickListener() {

        googleMap.setOnMarkerClickListener { marker ->

            val intent = Intent(requireActivity(), PokemonDetailsActivity::class.java)
            when (counter) {
                1 -> {
                    intent.putExtra(POKEMON_FOUND, PokemonFound(1, false, marker.position, true))
                }
                2 -> {
                    intent.putExtra(POKEMON_FOUND, PokemonFound(1, true, marker.position, false))
                }

                else -> {
                    intent.putExtra(POKEMON_FOUND, PokemonFound(1, false, marker.position, false))
                    counter = 0
                }
            }
            counter++
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

    private fun addMarker(latLng: LatLng) {
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latLng.latitude, latLng.longitude))
                .title("Pokemon")
                .icon(
                    BitmapDescriptorFactory
                        .fromResource(R.drawable.icons8_pokeball_96)
                )
        )

        marker?.tag = "bulbasaur"
    }

    fun generateRandomCoordinates(min: Int, max: Int): LatLng {
        // Get the Current Location's longitude and latitude
        val currentLong: Double = mainViewModel.currentLocationLiveData.value!!.longitude
        val currentLat: Double = mainViewModel.currentLocationLiveData.value!!.latitude

        // 1 KiloMeter = 0.00900900900901° So, 1 Meter = 0.00900900900901 / 1000
        val meterCord = 0.00900900900901 / 1000

        //Generate random Meters between the maximum and minimum Meters
        val r = Random()
        val randomMeters: Int = r.nextInt(max + min)

        //then Generating Random numbers for different Methods
        val randomPM: Int = r.nextInt(6)

        //Then we convert the distance in meters to coordinates by Multiplying number of meters with 1 Meter Coordinate
        val metersCordN = meterCord * randomMeters.toDouble()

        //here we generate the last Coordinates
        return if (randomPM == 0) {
            LatLng(currentLat + metersCordN, currentLong + metersCordN)
        } else if (randomPM == 1) {
            LatLng(currentLat - metersCordN, currentLong - metersCordN)
        } else if (randomPM == 2) {
            LatLng(currentLat + metersCordN, currentLong - metersCordN)
        } else if (randomPM == 3) {
            LatLng(currentLat - metersCordN, currentLong + metersCordN)
        } else if (randomPM == 4) {
            LatLng(currentLat, currentLong - metersCordN)
        } else {
            LatLng(currentLat - metersCordN, currentLong)
        }
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
        private const val TOTAL_POKEMONS = 7

        //In meters
        private const val MIN_DISTANCE = 1000
        private const val MAX_DISTANCE = 3000
    }

}