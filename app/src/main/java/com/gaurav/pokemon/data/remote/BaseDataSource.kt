package com.gaurav.pokemon.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ResponseHandler<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResponseHandler.success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    /** We can deserialize error model (in case we get error msg from server)
                     * and pass the message */
                    is HttpException -> {
                        ResponseHandler.error(
                            false,
                            throwable.code().toString(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        ResponseHandler.error(
                            true, null, null
                        )
                    }
                }
            }
        }
    }
}

