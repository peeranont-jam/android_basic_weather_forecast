package com.example.basicweatherforecast.data.model

data class Geolocation(
    var locations: List<GeolocationInfo>,
)

data class GeolocationInfo(
    var name: String,
    var lat: Double,
    var lon: Double,
    var country: String,
    var state: String,
)