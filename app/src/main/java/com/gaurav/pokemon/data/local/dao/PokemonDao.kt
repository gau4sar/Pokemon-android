package com.gaurav.pokemon.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.pokemon.data.model.PokemonInfo
import com.gaurav.pokemon.data.model.PokemonDetails

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonInfo: List<PokemonInfo>)

    @Query("SELECT * FROM pokemon_list")
    fun fetchPokemonList(): LiveData<List<PokemonInfo>>

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemonDetails: PokemonDetails)

    @Query("SELECT * FROM pokemon_details WHERE id = :pokemon_id LIMIT 1")
    fun fetchPokemonDetails(pokemon_id: Int): LiveData<PokemonDetails>*/

}