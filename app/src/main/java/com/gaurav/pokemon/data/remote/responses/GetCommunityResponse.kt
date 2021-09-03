package com.gaurav.pokemon.data.remote.responses

import com.gaurav.pokemon.data.model.Foe
import com.gaurav.pokemon.data.model.Friend

data class GetCommunityResponse (
    val friends : ArrayList<Friend>,
    val foes : ArrayList<Foe>
)
