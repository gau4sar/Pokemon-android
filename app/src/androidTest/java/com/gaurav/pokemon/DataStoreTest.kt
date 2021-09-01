package com.gaurav.pokemon

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Before
import java.io.File


@ExperimentalCoroutinesApi
abstract class DataStoreTest : CoroutineTest() {

    private val DS_FILE_NAME = "preferences-file-test"
    private val DIRECTORY_NAME = "dataStore"

    private lateinit var preferenceScope: CoroutineScope
    protected lateinit var dataStore: DataStore<Preferences>

    @Before
    fun createDataStore() {
        preferenceScope = CoroutineScope(testDispatcher + Job())

        dataStore = PreferenceDataStoreFactory.create(scope = preferenceScope) {
            InstrumentationRegistry.getInstrumentation()
                .targetContext.preferencesDataStoreFile(DS_FILE_NAME)
        }
    }

    @After
    fun deleteDataStore() {
        File(ApplicationProvider.getApplicationContext<Context>()
            .filesDir, DIRECTORY_NAME)
            .deleteRecursively()

        preferenceScope.cancel()
    }
}