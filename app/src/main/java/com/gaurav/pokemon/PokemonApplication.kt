package com.gaurav.pokemon

import android.app.Application
import com.gaurav.pokemon.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class PokemonApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Enable logging for only debug hence we don't use the default Logger class anywhere
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())


        // start Koin!
        startKoin {

            // Use Koin Android Logger
            androidLogger(Level.NONE)

            // declare used Android context
            androidContext(this@PokemonApplication)

            // declare modules
            modules(
                listOf(
                    appModule,
                    networkModule,
                    localDataModule,
                    viewModelModule,
                    preferencesModule
                )
            )
        }
    }
}
