package com.gaurav.pokemon.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class DataStorePreferences(
    private val dataStore: DataStore<Preferences>,
    private val apiTokenKey: Preferences.Key<String>
) {

    val apiToken = dataStore.getValueFlow(apiTokenKey, "")

    suspend fun saveApiToken(token: String) {
        if (token.isEmpty()) {
            dataStore.setValue(apiTokenKey, "")
        } else {
            dataStore.setValue(apiTokenKey, token)
        }
    }

}