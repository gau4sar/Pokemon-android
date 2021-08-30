package com.gaurav.pokemon.di

import com.gaurav.pokemon.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule by lazy {
    module {
        viewModel { MainViewModel(get()) }
    }
}