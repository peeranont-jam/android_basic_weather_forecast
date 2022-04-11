package com.example.basicweatherforecast.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherInfo(
    var lat: Double,
    var long: Double,
    var current: Current,
    var hourly: List<Hourly>,
    var cityName: String? = null,
    var unit: TemperatureUnit? = null
) : Parcelable

@Parcelize
data class Current(
    var temp: Double,
    var humidity: Double
) : Parcelable

@Parcelize
data class Hourly(
    var dateTime: Long,
    var temp: Double,
    var humidity: Double,
    var weather: List<Weather>
) : Parcelable

@Parcelize
data class Weather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
) : Parcelable