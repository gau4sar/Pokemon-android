package com.gaurav.pokemon.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.pokemon.data.model.ApiToken
import io.reactivex.Single

@Dao
interface FirebaseApiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTokenInfo(apiToken: ApiToken)

    @Query("SELECT expiresAt FROM apiToken")
    fun fetchExpiresAt(): Livedata<Double>

    @Query("SELECT * FROM apiToken")
    fun fetchTokenInfo(): LiveData<ApiToken>
}