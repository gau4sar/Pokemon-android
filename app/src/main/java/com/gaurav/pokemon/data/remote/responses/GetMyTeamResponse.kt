package com.gaurav.pokemon.data.remote.responses

import com.gaurav.pokemon.data.model.MyTeam

data class GetMyTeamResponse (
    val myTeamList : ArrayList<MyTeam>
)