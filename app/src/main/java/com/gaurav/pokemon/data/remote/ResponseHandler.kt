package com.gaurav.pokemon.data.remote

import okhttp3.ResponseBody

/**
 * A generic class that wraps API a response data object
 */
sealed class ResponseHandler<out T> {

    data class Success<out T>(val data: T?) : ResponseHandler<T>()

    data class Error(
        val isNetworkError: Boolean,
        val message: String?,
        val data: ResponseBody?
    ) : ResponseHandler<Nothing>()

    data class Loading<out T>(val data: T?) : ResponseHandler<T>()

    companion object {
        fun <T> loading(value: T?): ResponseHandler<T> = Loading(value)

        fun <T> success(value: T): ResponseHandler<T> = Success(value)

        fun <T> error(
            isNetworkError: Boolean,
            errorMessage: String?,
            data: ResponseBody?
        ): ResponseHandler<T> = Error(isNetworkError, errorMessage, data)
    }
}