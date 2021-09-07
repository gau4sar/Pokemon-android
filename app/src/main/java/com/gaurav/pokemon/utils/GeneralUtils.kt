package com.gaurav.pokemon.utils

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.palette.graphics.Palette
import com.gaurav.pokemon.data.model.PokemonDetails
import com.gaurav.pokemon.data.model.PokemonList
import com.gaurav.pokemon.data.model.PokemonLocationInfo
import com.gaurav.pokemon.data.model.pokemon.Type
import com.gaurav.pokemon.ui.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.Constants.NA
import com.gaurav.pokemon.utils.Constants.POKEMON_DETAILS
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

    var suffixes = arrayOf(
        "th",
        "st",
        "nd",
        "rd",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "st",
        "nd",
        "rd",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "th",
        "st"
    )

    fun parseDateToShortMonthDateAndYear(date: String): String {
        return if (date.isEmpty()) {
            NA
        } else {
            //2020-02-16T00:00:00.000Z
            val outputPattern = "MMM dd---,yyyy"

            parseDates(date, outputPattern).replace("---", suffixes[parseDateToD(date).toInt()])
        }
    }

    fun parseDateToD(date: String): String {
        //2020-02-16T00:00:00.000Z
        val outputPattern = "d"

        return parseDates(date, outputPattern)
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

    fun intentPokemonDetails(
        context: FragmentActivity,
        pokemonLocationInfo: PokemonLocationInfo,
        pokemonStatus: Int,
        capturedBy: String,
        transitionImage: View? = null
    ) {
        val intent = Intent(context, PokemonDetailsActivity::class.java)
        val pokemonDetails = PokemonDetails(pokemonLocationInfo, pokemonStatus, capturedBy)
        val bundle = Bundle()

        bundle.putSerializable(POKEMON_DETAILS, pokemonDetails)
        intent.putExtras(bundle)

        if (transitionImage != null) {
            // create an options object that defines the transition
            val options = ActivityOptions
                .makeSceneTransitionAnimation(context, transitionImage, "pokemon_image")
            context.startActivity(intent, options?.toBundle())
        } else {
            context.startActivity(intent)
        }

    }

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

    fun parseTypeToColor(type: Type): Long {
        return when (type.type.name.lowercase(Locale.ROOT)) {
            "normal" -> TypeNormal
            "fire" -> TypeFire
            "water" -> TypeWater
            "electric" -> TypeElectric
            "grass" -> TypeGrass
            "ice" -> TypeIce
            "fighting" -> TypeFighting
            "poison" -> TypePoison
            "ground" -> TypeGround
            "flying" -> TypeFlying
            "psychic" -> TypePsychic
            "bug" -> TypeBug
            "rock" -> TypeRock
            "ghost" -> TypeGhost
            "dragon" -> TypeDragon
            "dark" -> TypeDark
            "steel" -> TypeSteel
            "fairy" -> TypeFairy
            else -> Color.BLACK.toLong()
        }
    }
}