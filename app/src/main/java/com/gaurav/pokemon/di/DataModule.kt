package com.gaurav.pokemon.di

import android.app.Application
import com.gaurav.pokemon.data.local.PokemonRoomDb
import com.gaurav.pokemon.data.local.dao.PokemonDao
import com.gaurav.pokemon.utils.Constants
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localDataModule by lazy {
    module {
        single { provideRoomDatabase(androidApplication()) }
        single { provideFirebaseApiDao(get()) }
        single { providePokemonDao(get()) }
    }
}

fun provideRoomDatabase(application: Application): PokemonRoomDb {
    return PokemonRoomDb.invoke(application.applicationContext)
}

fun provideFirebaseApiDao(pokemonRoomDb: PokemonRoomDb) = pokemonRoomDb.firebaseDao()

fun providePokemonDao(pokemonRoomDb: PokemonRoomDb) = pokemonRoomDb.pokemonDao()