package com.gaurav.pokemon.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gaurav.pokemon.R
import androidx.core.content.ContextCompat
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.ui.main.MainViewModel
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    var deniedPermanently = true

    val mainViewModel by viewModel<MainViewModel>()

    private val POKEMON_ITEM_LIMIT = "10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeTheLocationRequest()
        listenTheLocationCallBack()

        /*mainViewModel.observePokemonInfoList(POKEMON_ITEM_LIMIT).observe(this, { apiResponse ->

            when (apiResponse) {

                is ResponseHandler.Success -> {

                    apiResponse.data?.let { getPokemonInfoListResponse ->
                        Timber.d("Pokemon info list ${getPokemonInfoListResponse}")
                    }
                }

                is ResponseHandler.Error -> {
                    Timber.e("Get token info error response: $apiResponse")
                    //handleApiError(apiResponse, this)
                }

                is ResponseHandler.Loading -> {
                }
            }
        })*/
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isLocationPermissionGranted():Boolean {
        if (checkIfUserPermissionIsNotProvided()
        ) {
            requestPermission()
        } else {
            requestLocation()
        }
        return !checkIfUserPermissionIsNotProvided()
    }

    private fun listenTheLocationCallBack() {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    Timber.d("locationResult null")
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        Timber.d("locationResult not null")
                        mainViewModel.currentLocationLiveData.postValue(location)
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient?.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }
        }
    }

    private fun initializeTheLocationRequest() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest?.interval = (10 * 1000).toLong() // 10 seconds
        locationRequest?.fastestInterval = (5 * 1000).toLong() // 5 seconds
    }

    //request location
    private fun requestLocation() {
        Timber.d("requestLocation")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
            if (location != null) {
                mainViewModel.currentLocationLiveData.postValue(location)
            } else {
                mFusedLocationClient?.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            }
        }
    }

    //on request permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.LOCATION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Timber.d("permission granted")
                    requestLocation()
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) && ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    ) {
                        deniedPermanently = false
                        Timber.d("permission denied not permanently")
                    } else {

                        if (deniedPermanently) {
                            Timber.d("permission denied permanently")
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "You have to allow location permission in settings",
                                Snackbar.LENGTH_LONG
                            ).apply {
                                setAction("Go") {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivity(intent)
                                }
                                setActionTextColor(ContextCompat.getColor(context, R.color.white))
                                show()
                            }
                        }
                        deniedPermanently = true
                    }
                }
            }
        }
    }
}