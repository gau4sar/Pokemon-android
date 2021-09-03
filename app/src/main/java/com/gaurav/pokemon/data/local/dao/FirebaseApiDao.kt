package com.gaurav.pokemon.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.model.Friend

@Dao
interface FirebaseApiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTokenInfo(apiTokenInfo: ApiTokenInfo)

    @Query("SELECT * FROM apiToken")
    fun fetchTokenInfo(): LiveData<ApiTokenInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendsList(friends: List<Friend>)

    @Query("SELECT * FROM friend")
    fun fetchFriendsList(): LiveData<List<Friend>>

}