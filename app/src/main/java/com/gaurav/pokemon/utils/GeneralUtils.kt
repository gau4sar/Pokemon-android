package com.gaurav.pokemon.utils

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.model.PokemonList
import com.gaurav.pokemon.ui.main.pokemon_details.PokemonDetailsActivity
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object GeneralUtils {

    fun getAuthToken(encryptPrefUtils: EncryptPrefUtils) = encryptPrefUtils.getApiToken()

    fun pickPokemonsRandomly(
        totalPokemons: Int, pokemonList: List<PokemonList>,
        sendRandomPokemonList: (MutableList<PokemonList>) -> Unit = {}
    ) {


        val randomlySelectedPokemons = mutableListOf<PokemonList>()

        val mutablePokemonList = pokemonList.toMutableList()

        for (i in 1..totalPokemons) {
            mutablePokemonList.apply {
                val randomPokemon = this.random()
                randomlySelectedPokemons.add(randomPokemon)

                this.remove(randomPokemon)
            }
        }

        val listOfIds: List<String> = mutablePokemonList.map {
            it.name
        }

        Timber.d("Pokemon list after removing -> ${listOfIds}")
        Timber.d("Random Pokemon list -> ${randomlySelectedPokemons}")

        sendRandomPokemonList(randomlySelectedPokemons)
    }

    fun generateRandomMarkers(
        totalPokemons: Int,
        currentLocation: Location,
        minimumDistanceFromMe: Int,
        maximumDistanceFromMe: Int,
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


    fun parseDateToShortMonthDateAndYear(date: String): String {
        //2020-02-16T00:00:00.000Z
        val outputPattern = "MMM dd---,yyyy"

        return parseDates(date, outputPattern).replace("---", "th")
    }

    fun parseDates(dateString: String, outputPattern: String): String {
        val inputPattern = "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun getPokemonImageUrl(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
    }

    fun intentPokemonDetails(context : Context, pokemonId: Int,
                             pokemonName: String, pokemonStatus: Int) {
        val intent = Intent(context, PokemonDetailsActivity::class.java)
        val bundle = Bundle()

        bundle.putInt(Constants.POKEMON_ID, pokemonId)
        bundle.putString(Constants.POKEMON_NAME, pokemonName)
        bundle.putInt(Constants.POKEMON_STATUS, pokemonStatus)

        intent.putExtras(bundle)
        ContextCompat.startActivity(context, intent, bundle)

    }
}