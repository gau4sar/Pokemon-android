package com.gaurav.pokemon.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gaurav.pokemon.R
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeTheLocationRequest()

        listenTheLocationCallBack()

    }

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
    fun requestLocation() {
        Timber.d("requestLocation")
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
                    /*gotoSelectPlacesFragment()*/
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