package com.gaurav.pokemon.di

import com.gaurav.pokemon.ui.PokeApiViewModel
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.Constants
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule by lazy {
    module {
        viewModel { MainViewModel(get(), get(named(Constants.POKEAPI_SCOPE))) }

        viewModel { PokeApiViewModel(get(named(Constants.POKEAPI_SCOPE))) }
    }
}