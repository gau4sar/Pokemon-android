package com.gaurav.pokemon.utils

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gaurav.pokemon.DataStoreTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class DataStorePreferencesTest : DataStoreTest() {

    private val apiTokenValue = UUID.randomUUID().toString()
    private val tokenKey: Preferences.Key<String> = stringPreferencesKey("token")

    private lateinit var dataStorePreferences: DataStorePreferences

    @Before
    fun setup() {
        dataStorePreferences = DataStorePreferences(dataStore, tokenKey)
    }

    /**
     * Check if token is available
     */
    @Test
    fun checkIfTokenIsAvailable() = coTest {
        dataStore.edit { preferences ->
            preferences[tokenKey] = apiTokenValue
        }
    }


    /**
     * Check if latest token is saved to datastore
     */
    @Test
    fun saveTokenOverridesPreviousValue() = coTest {

        val previousToken = UUID.randomUUID().toString()

        dataStore.edit { preferences ->
            preferences[tokenKey] = previousToken
        }
        assertEquals(previousToken, tokenKey.findCurrentValue())

        dataStorePreferences.saveApiToken(apiTokenValue)
        assertEquals(apiTokenValue, tokenKey.findCurrentValue())
    }

    private suspend fun <T> Preferences.Key<T>.findCurrentValue() =
        dataStore.data.first()[this]


    @Test
    fun getApiToken() {
    }

    @Test
    fun saveApiToken() {
    }
}