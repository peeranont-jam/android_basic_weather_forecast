package com.example.basicweatherforecast.data.remote.model

import com.example.basicweatherforecast.data.model.Geolocation
import com.google.gson.annotations.SerializedName


data class GeolocationResponse(
    @SerializedName("name")
    var name: String,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lon")
    var lon: Double,
    @SerializedName("country")
    var country: String
) {
    fun mapToGeolocation(): Geolocation {
        return Geolocation(
            name = this.name,
            lat = this.lat,
            lon = this.lon,
            country = this.country
        )
    }
}
