package com.gaurav.pokemon.data.remote.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.util.*

open class PokeApiResponse {

    @SerializedName("success")
    open val successful: Boolean = false

    @SerializedName("data")
    val data: Any? = null

    @SerializedName("errors")
    var error: List<Objects>? = null

    @SerializedName("meta")
    var meta: JSONObject? = null

    @SerializedName("message")
    var message: String? = null
}

@Keep
data class Errors(
    val message: String,
    val type: String
)