package com.gaurav.pokemon.utils

object GeneralUtils {

    fun getAuthToken(encryptPrefUtils: EncryptPrefUtils) = encryptPrefUtils.getApiToken()

}