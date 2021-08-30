package com.gaurav.pokemon.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.databinding.CustomNetworkFailedBinding
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

fun <T, L> responseLiveData(
    roomQueryToRetrieveData: () -> LiveData<T>,
    networkRequest: suspend () -> ResponseHandler<L>,
    roomQueryToSaveData: suspend (L) -> Unit
): LiveData<ResponseHandler<T>> = liveData(Dispatchers.IO) {

    emit(ResponseHandler.loading(null))

    // Get data from room db
    val source = roomQueryToRetrieveData().map { ResponseHandler.success(it) }
    emitSource(source)
    Timber.d("Room query to get data : $latestValue")


    // Update data to room db from API
    when (val apiResponse = networkRequest()) {

        is ResponseHandler.Success -> {
            apiResponse.data?.let {
                Timber.d("networkRequest to get data : $source")
                roomQueryToSaveData(it)
            }
        }

        is ResponseHandler.Error -> {
            emit(
                ResponseHandler.error(
                    false,
                    apiResponse.message ?: "API response error",
                    null
                )
            )
            emitSource(source)
        }

        else -> {
            emit(
                ResponseHandler.error(
                    true,
                    "Something went wrong, please try again later",
                    null
                )
            )
            emitSource(source)
        }
    }
}


fun Fragment.handleApiError(
    error: ResponseHandler.Error,
    activity: FragmentActivity
) {
    when {
        error.isNetworkError -> {
            activity.checkNetworkConnection(activity as LifecycleOwner)
        }

        else -> {

            when (error.message) {
                // Handle expected scenarios. Example : Authentication failure, token expiry, etc
            }

            val errorMessage = error.data?.string().toString()
            activity.showLongToast(errorMessage)
        }
    }
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun Activity.checkNetworkConnection(lifecycleOwner: LifecycleOwner) {
    val networkConnection = NetworkConnection(applicationContext)
    networkConnection.observe(lifecycleOwner, { isNetworkConnected ->
        Timber.d("checkNetworkConnection isNetworkConnected : $isNetworkConnected")
        if (!isNetworkConnected) {
            val binding = CustomNetworkFailedBinding.inflate(layoutInflater)
            this.setContentView(binding.root)

            binding.buttonRetryConnection.setOnClickListener {
                Timber.d("Not connected to the network !! Retry called !!! ")
                this.recreate()
            }
        }
    })
}