package com.gaurav.pokemon.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.databinding.CustomNetworkFailedBinding
import com.gaurav.pokemon.utils.Constants.LOCATION_REQUEST_CODE
import com.gaurav.pokemon.utils.GeneralUtils.calcDominantColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun Activity.getDominantColor(url: String,imageView: ImageView?=null,
                             onLoadingFinished: (Int) -> Unit) {

    Glide.with(this)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                imageView?.setImageBitmap(resource)
                Timber.d("onResourceReady")
                calcDominantColor(resource){

                    onLoadingFinished(it)

                    Timber.d("calcDominantColor $it")
                }
            }
            override fun onLoadCleared(placeholder: Drawable?) {
                // this is called when imageView is cleared on lifecycle call or for
                // some other reason.
                // if you are referencing the bitmap somewhere else too other than this imageView
                // clear it here as you can no longer have the bitmap
            }
        })


/*
        image.setDrawingCacheEnabled(true)

        val bitmap = image.drawable.toBitmap(50,50)

        val icon = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.icons8_pokeball_96
        )

        Palette.Builder(image.getDrawingCache()).generate { it?.let { palette ->
            val dominantColor = palette.getDominantColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

            // TODO: use dominant color

            Timber.d("dominantColor ${dominantColor}")

            binding.clMain.setBackgroundColor(dominantColor)

        } }*/
}
//Glide
fun ImageView.load(
    url: String?,
    fragmentActivity: FragmentActivity,
    isCircularCropImage: Boolean = false,
    isWithThumbnail: Boolean = false,
    isCircularImage: Boolean = false,
    customOption:RequestOptions? = null,
    onLoadingFinished: () -> Unit = {}
) {
    val targetImageView = this
    val listener = object : RequestListener<Drawable> {
        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            dataSource: com.bumptech.glide.load.DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }
    }
    if (url.isNullOrEmpty()) {
        targetImageView.loadNoImage(fragmentActivity)
    }
    else if(url.contains("null"))
    {
        targetImageView.loadNoImage(fragmentActivity)
    }
    else {

        val glide = Glide.with(this)
            .load(url)
            .apply(
                getOptionsForGlide(context, fragmentActivity)
            )
            .listener(listener)
            .transition(DrawableTransitionOptions.withCrossFade())
        if (isWithThumbnail) {
            if (isCircularImage) {
                glide.circleCrop()
                    .thumbnail(0.05f)
                    .into(this)

            }
            else if(isCircularCropImage)
            {
                glide.thumbnail(0.05f)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this)
            }
            else {
                glide.thumbnail(0.05f)
                    .into(this)
            }
        } else {
            if (isCircularImage) {
                glide.circleCrop()
                    .into(this)
            }
            else if(isCircularCropImage)
            {
                glide.apply(RequestOptions.circleCropTransform())
                    .into(this)
            } else {
                glide.into(this)
            }
        }
    }
}

private fun ImageView.loadNoImage(fragmentActivity: FragmentActivity) {
    Glide.with(this)
        .load(R.drawable.icons8_pokeball_96)
        .apply(
            getOptionsForGlide(context, fragmentActivity)
        )
        .into(this)
}

fun getOptionsForGlide(context: Context, fragmentActivity: FragmentActivity): RequestOptions {
    //Options
    return RequestOptions()
        .error(R.drawable.icons8_pokeball_96)
        .placeholder(getCustomProgressBar(context, fragmentActivity))
}

fun getCustomProgressBar(
    context: Context,
    fragmentActivity: FragmentActivity
): CircularProgressDrawable {
    //Custom Progress Bar for glide while loading the image
    val circularProgressDrawable = CircularProgressDrawable(fragmentActivity)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        circularProgressDrawable.setColorSchemeColors(context.getColor(R.color.colorPrimary))
    } else
        circularProgressDrawable.setColorSchemeColors(context.resources.getColor(R.color.colorPrimary))
    circularProgressDrawable.start()
    return circularProgressDrawable
}


/*fun ImageView.load(url:String, fragmentActivity:Activity)
{
    Glide.with(fragmentActivity)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}*/

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
                Timber.d("networkRequest response : ${apiResponse.data}")
                roomQueryToSaveData(it)
            }
        }

        is ResponseHandler.Error -> {
            emit(
                ResponseHandler.error(
                    false,
                    apiResponse.message ?: "Failed to fetch latest data from API",
                    null
                )
            )
            emitSource(source)
        }

        else -> {
            emit(
                ResponseHandler.error(
                    true,
                    "Check your internet connection",
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

            // Handle expected scenarios. Based on error message
            // Example : Authentication failure, token expiry, etc
            error.message?.let {
                activity.showLongToast(it)
            }

            // Handle the data sent my error
            val errorData = error.data.toString()
            Timber.e("handleApiError data: $errorData")
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

/**
 * Jetpack DataStore helpers
 */

// Value is gfg here
fun <gfg> DataStore<Preferences>.getValueFlow(
    your_key: Preferences.Key<gfg>,
    someDefaultValue: gfg
): Flow<gfg> {
    return this.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                // Exception handling
                throw exception
            }
        }.map { preferences ->
            preferences[your_key] ?: someDefaultValue
        }
}

suspend fun <gfg> DataStore<Preferences>.setValue(key: Preferences.Key<gfg>, value: gfg) {
    this.edit { preferences ->
        preferences[key] = value
    }
}

suspend fun <gfg> DataStore<Preferences>.getValue(key: Preferences.Key<gfg>): gfg? {
    val preferences = this.data.first()
    return preferences[key]
}

/*
    Date util method
 */
fun getFormattedDateTime(dateTimeString: String): String {
    val parsedDate = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.format(DateTimeFormatter.ofPattern(Constants.CONVERTED_DATE_FORMAT))
}

fun Context.showToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
}

fun Activity.enableGps(): Unit {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage("Enable GPS").setCancelable(false)
        .setPositiveButton(
            "Yes"
        ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        .setNegativeButton(
            "No"
        ) { dialog, _ -> dialog.cancel() }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}

fun Activity.isGpsEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Activity.checkAndEnableGps() {
    if (!isGpsEnabled()) {
        enableGps()
    }
}

fun Activity.checkIfUserPermissionIsNotProvided(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.requestPermission() {
    requestPermissions(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), LOCATION_REQUEST_CODE
    )
}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun String.make1stCharacterUpper():String
{
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}