package com.gaurav.pokemon.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.pokemon.data.model.PokemonInfo

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonInfo: List<PokemonInfo>)

    @Query("SELECT * FROM pokemon_list")
    fun fetchPokemonList(): LiveData<List<PokemonInfo>>

}