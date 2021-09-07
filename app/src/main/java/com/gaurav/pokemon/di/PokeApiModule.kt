package com.gaurav.pokemon.di

import android.content.Context
import com.gaurav.pokemon.data.remote.pokemon.PokeApiService
import com.gaurav.pokemon.data.remote.pokemon.PokemonApiRemoteDataSource
import com.gaurav.pokemon.data.repository.PokemonApiRepository
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.Constants.POKEAPI_SCOPE
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

val pokeModule = module {

    single(named(POKEAPI_SCOPE)) {
        providePokemonOkHttpClient(androidContext())
    }

    single(named(POKEAPI_SCOPE)) {
        providePokemonRetrofit(
            get(named(POKEAPI_SCOPE)),
            get()
        )
    }

    single(named(POKEAPI_SCOPE)) {
        providePokemonApiService(get(named(POKEAPI_SCOPE)))
    }

    single(named(POKEAPI_SCOPE)) {
        PokemonApiRemoteDataSource(get((named(POKEAPI_SCOPE))))
    }

    single(named(POKEAPI_SCOPE)) {
        PokemonApiRepository(get(named(POKEAPI_SCOPE)), get())
    }

}

fun providePokemonOkHttpClient(context: Context): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.i(message) }
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val cacheFile = File(context.cacheDir, "okhttp.cache")
    val cache = Cache(cacheFile, (10 * 1000 * 1000).toLong()) // 10 MB

    return OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .cache(cache)
        .build()
}

fun providePokemonRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.POKEMON_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
}

fun providePokemonApiService(retrofit: Retrofit): PokeApiService =
    retrofit.create(PokeApiService::class.java)
