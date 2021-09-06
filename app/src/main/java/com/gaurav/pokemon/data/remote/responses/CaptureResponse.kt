package com.gaurav.pokemon.data.remote.responses

import com.google.gson.annotations.SerializedName
import java.util.*

data class CaptureResponse(

    @SerializedName("success")
    val successful: Boolean = false,

    @SerializedName("data")
    val data: Any? = null,

    @SerializedName("errors")
    var error: List<Objects>? = null,

    @SerializedName("message")
    var message: String? = null
)