package com.gaurav.pokemon.di

import android.app.Application
import com.gaurav.pokemon.data.local.PokemonRoomDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataModule by lazy {
    module {
        single { provideRoomDatabase(androidApplication()) }
        single { provideFirebaseApiDao(get()) }
    }
}

fun provideRoomDatabase(application: Application): PokemonRoomDb {
    return PokemonRoomDb.invoke(application.applicationContext)
}

fun provideFirebaseApiDao(pokemonRoomDb: PokemonRoomDb) = pokemonRoomDb.getFirebaseApiDao()