package com.gaurav.pokemon.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptPrefUtils(val context: Context) {

    private var masterKey: MasterKey =
        MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    private var encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        ENCRYPTED_PREFS_FILE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveApiToken(token: String) {
        encryptedSharedPreferences
            .edit().putString(API_TOKEN, token).apply()
    }

    fun getApiToken(): String? {
        return encryptedSharedPreferences.getString(API_TOKEN, "")
    }

    companion object {
        const val ENCRYPTED_PREFS_FILE = "prefs"
        const val API_TOKEN = "api_token"
    }
}