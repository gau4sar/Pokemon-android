package com.gaurav.pokemon.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.runBlocking

class DataStorePreferences(
    private val dataStore: DataStore<Preferences>,
    private val apiTokenKey: Preferences.Key<String>
) {

    val apiToken = runBlocking {
        dataStore.getValue(apiTokenKey)
    }

    suspend fun saveApiToken(token: String) {

        if (token.isEmpty()) {
            dataStore.setValue(apiTokenKey, "")
        } else {
            dataStore.setValue(apiTokenKey, token)
        }
    }

}