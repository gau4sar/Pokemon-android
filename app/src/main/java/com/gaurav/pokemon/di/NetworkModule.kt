package com.gaurav.pokemon.di

import android.content.Context
import com.gaurav.pokemon.data.remote.FirebaseApiRemoteDataSource
import com.gaurav.pokemon.data.remote.FirebaseApiService
import com.gaurav.pokemon.data.repository.FirebaseApiRepository
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.DataStorePreferences
import com.gaurav.pokemon.utils.EncryptPrefUtils
import com.gaurav.pokemon.utils.GeneralUtils
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

val networkModule = module {

    single(named(Constants.DEFAULT_SCOPE)) { provideDefaultOkHttpClient(androidContext(), get()) }
    single(named(Constants.DEFAULT_SCOPE)) { provideRetrofit(get((named(Constants.DEFAULT_SCOPE))), get()) }

    single { provideFirebaseAPIService(get(named(Constants.DEFAULT_SCOPE))) }

    single { FirebaseApiRepository(get(), get()) }
    single { FirebaseApiRemoteDataSource(get()) }
}

fun provideDefaultOkHttpClient(context: Context, preferences: EncryptPrefUtils): OkHttpClient {
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
                .addHeader("Authorization", "Bearer ${GeneralUtils.getAuthToken(preferences)}")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .cache(cache)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
}

fun provideFirebaseAPIService(retrofit: Retrofit) : FirebaseApiService =
    retrofit.create(FirebaseApiService::class.java)