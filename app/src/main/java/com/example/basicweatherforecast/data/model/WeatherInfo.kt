package com.example.basicweatherforecast.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class WeatherInfo(
    var lat: Double,
    var long: Double,
    var current: Current,
    var hourly: List<Hourly>,
    var cityName: String? = null,
    var unit: TemperatureUnit? = null
) : Parcelable

@Keep
@Parcelize
data class Current(
    var temp: Double,
    var humidity: Double
) : Parcelable

@Keep
@Parcelize
data class Hourly(
    var dateTime: Long,
    var temp: Double,
    var humidity: Double,
    var weather: List<Weather>
) : Parcelable

@Keep
@Parcelize
data class Weather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
) : Parcelable