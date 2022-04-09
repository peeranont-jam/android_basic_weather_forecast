package com.example.basicweatherforecast.data.remote.model

import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.data.model.GeolocationInfo
import com.google.gson.annotations.SerializedName

data class GeolocationResponse(
    var locations: List<GeolocationModel>,
) {
    fun mapToGeolocation(): Geolocation {
        return Geolocation(
            locations.map {
                GeolocationInfo(
                    name = it.name,
                    lat = it.lat,
                    lon = it.lon,
                    country = it.country,
                    state = it.state
                )
            }
        )
    }
}


data class GeolocationModel(
    @SerializedName("name")
    var name: String,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lon")
    var lon: Double,
    @SerializedName("country")
    var country: String,
    @SerializedName("state")
    var state: String,
)