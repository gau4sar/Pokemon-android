package com.gaurav.pokemon.data.local.converters

import androidx.room.TypeConverter
import com.gaurav.pokemon.data.model.Friend
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent

class FriendListConverters {
    private val gson: Gson by KoinJavaComponent.inject(Gson::class.java)

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
}