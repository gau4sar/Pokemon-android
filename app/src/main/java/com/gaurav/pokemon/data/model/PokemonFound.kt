package com.gaurav.pokemon.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonFound(val pokemon_id:Int, val isWild:Boolean, val pokemon_location:LatLng, val isCapturedByOther:Boolean):Parcelable