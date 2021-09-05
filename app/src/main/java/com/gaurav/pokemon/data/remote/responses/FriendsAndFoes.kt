package com.gaurav.pokemon.data.remote.responses

import androidx.room.Entity
import com.gaurav.pokemon.data.model.Foe
import com.gaurav.pokemon.data.model.Friend
import java.io.Serializable

@Entity(tableName = "friendsAndFoes", primaryKeys = ["friends", "foes"])
data class FriendsAndFoes(
    val friends: ArrayList<Friend>,
    val foes: ArrayList<Foe>
): Serializable
