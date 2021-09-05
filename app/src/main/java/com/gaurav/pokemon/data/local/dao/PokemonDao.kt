package com.gaurav.pokemon.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.pokemon.data.model.PokemonList

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonList>)

    @Query("SELECT * FROM pokemon_list")
    fun fetchPokemonList(): LiveData<List<PokemonList>>

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemonDetails: PokemonDetails)

    @Query("SELECT * FROM pokemon_details WHERE id = :pokemon_id LIMIT 1")
    fun fetchPokemonDetails(pokemon_id: Int): LiveData<PokemonDetails>*/

}