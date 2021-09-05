package com.gaurav.pokemon.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.pokemon.data.model.*
import com.gaurav.pokemon.data.remote.responses.FriendsAndFoes

@Dao
interface FirebaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTokenInfo(apiTokenInfo: ApiTokenInfo)

    @Query("SELECT * FROM apiToken")
    fun fetchTokenInfo(): LiveData<ApiTokenInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunityActivity(friends: FriendsAndFoes)

    @Query("SELECT * FROM friendsAndFoes")
    fun fetchCommunityActivity(): LiveData<FriendsAndFoes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyTeamList(myTeamList: List<MyTeam>)

    @Query("SELECT * FROM myTeam")
    fun fetchMyTeamList(): LiveData<List<MyTeam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCapturedList(capturedList: List<PokemonLocationInfo>)

    @Query("SELECT * FROM pokemonLocationInfo")
    fun fetchCapturedList(): LiveData<List<PokemonLocationInfo>>

}