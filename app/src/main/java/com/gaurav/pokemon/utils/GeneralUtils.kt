package com.gaurav.pokemon.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber
import java.util.*

object GeneralUtils {

    fun getAuthToken(encryptPrefUtils: EncryptPrefUtils) = encryptPrefUtils.getApiToken()

    fun generateRandomMarkers(
        totalPokemons: Int,
        currentLocation: Location,
        minimumDistanceFromMe:Int,
        maximumDistanceFromMe:Int,
        randomCoordintes: (LatLng) -> Unit = {}
    ) {
        //set number of markers you want to generate in Map/
        val markersToGenerate = totalPokemons
        for (position in 1..markersToGenerate) {
            val coordinates: LatLng =
                generateRandomCoordinates(
                    LatLng(
                        currentLocation.latitude,
                        currentLocation.longitude
                    ), minimumDistanceFromMe, maximumDistanceFromMe
                )
            Timber.d("random_coordinates $coordinates")
            randomCoordintes(coordinates)
        } // end FOR loop
    }

    fun generateRandomCoordinates(currentLocation: LatLng, min: Int, max: Int): LatLng {
        // Get the Current Location's longitude and latitude
        val currentLong: Double = currentLocation.longitude
        val currentLat: Double = currentLocation.latitude

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

}