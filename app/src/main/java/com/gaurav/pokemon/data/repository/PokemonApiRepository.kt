package com.gaurav.pokemon.data.repository

import com.gaurav.pokemon.data.local.dao.PokemonDao
import com.gaurav.pokemon.data.remote.pokemon.PokemonApiRemoteDataSource
import com.gaurav.pokemon.utils.responseLiveData

class PokemonApiRepository(
    private val pokemonApiRemoteDataSource: PokemonApiRemoteDataSource,
    private val pokemonDao: PokemonDao
) {

    fun observePokemonInfoList(limit: String) = responseLiveData(
        roomQueryToRetrieveData = { pokemonDao.fetchPokemonList() },
        networkRequest = { pokemonApiRemoteDataSource.getPokemonInfoList(limit) }
    ) { pokemonDao.insertPokemonList(it.results) }

    val fetchPokemonInfoList = pokemonDao.fetchPokemonList()

    suspend fun fetchPokemonDetails(name: String) = pokemonApiRemoteDataSource.getPokemonDetails(name)
}