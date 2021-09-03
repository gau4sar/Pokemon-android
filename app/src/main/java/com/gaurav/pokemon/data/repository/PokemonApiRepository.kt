package com.gaurav.pokemon.data.repository

import com.gaurav.pokemon.data.local.dao.PokemonDao
import com.gaurav.pokemon.data.remote.pokemon.PokemonApiRemoteDataSource
import com.gaurav.pokemon.utils.responseLiveData

class PokemonApiRepository(
    private val pokemonApiRemoteDataSource: PokemonApiRemoteDataSource,
    private val pokemonDao: PokemonDao
) {

}