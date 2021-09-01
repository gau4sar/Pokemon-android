package com.gaurav.pokemon.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.gaurav.pokemon.utils.Constants.AUTH_TOKEN
import com.gaurav.pokemon.utils.Constants.DS_PREFS
import com.gaurav.pokemon.utils.DataStorePreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {

    factory {
        stringPreferencesKey(AUTH_TOKEN)
    }

    single {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(DS_PREFS)
        }
    }

    single { DataStorePreferences(get(), get()) }

}