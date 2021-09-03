package com.gaurav.pokemon.data.local

import androidx.room.TypeConverter
import com.gaurav.pokemon.data.model.Friend
import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.model.PokemonInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent.inject

class ListConverters {

    private val gson: Gson by inject(Gson::class.java)

    @TypeConverter
    fun pokemonInfoListToJson(pokemonInfoList: List<PokemonInfo>?): String? {
        val type = object : TypeToken<PokemonInfo>() {}.type
        return gson.toJson(pokemonInfoList, type)
    }

    @TypeConverter
    fun pokemonInfoJsonToList(pokemonInfoString: String): List<PokemonInfo>? {
        val type = object : TypeToken<List<PokemonInfo>>() {}.type
        return gson.fromJson(pokemonInfoString, type)
    }

    @TypeConverter
    fun pokemonObjectToJson(pokemon: Pokemon): String? {
        val type = object : TypeToken<Pokemon>() {}.type
        return gson.toJson(pokemon, type)
    }

    @TypeConverter
    fun pokemonJsonToList(pokemonString: String): Pokemon? {
        val type = object : TypeToken<Pokemon>() {}.type
        return gson.fromJson(pokemonString, type)
    }
}