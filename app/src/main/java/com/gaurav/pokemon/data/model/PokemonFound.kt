package com.gaurav.pokemon.data.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable


data class PokemonFound(
    val pokemon_id: Int,
    val isWild: Boolean, val pokemon_location: LatLng, val isCapturedByOther: Boolean
) : Serializable