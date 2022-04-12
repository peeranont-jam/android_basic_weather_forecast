package com.example.basicweatherforecast.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Geolocation(
    var name: String,
    var lat: Double,
    var lon: Double,
    var country: String? = null,
    var state: String? = null
) : Parcelable