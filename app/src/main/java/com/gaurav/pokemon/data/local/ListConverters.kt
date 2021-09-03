package com.gaurav.pokemon.data.local

import androidx.room.TypeConverter
import com.gaurav.pokemon.data.model.Friend
import com.gaurav.pokemon.data.model.Pokemon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent.inject

class ListConverters {

    private val gson: Gson by inject(Gson::class.java)

    @TypeConverter
    fun friendListToJson(friends: List<Friend>?): String? {
        val type = object : TypeToken<Friend>() {}.type
        return gson.toJson(friends, type)
    }

    @TypeConverter
    fun friendJsonToList(friendString: String): List<Friend>? {
        val type = object : TypeToken<List<Friend>>() {}.type
        return gson.fromJson(friendString, type)
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