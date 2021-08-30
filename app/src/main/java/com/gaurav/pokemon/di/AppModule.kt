package com.gaurav.pokemon.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

/**
 * Koin module to instantiate app generic classes like Shared Preferences, GSON, etc
 */
val appModule = module {
    single { provideGson() }
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}