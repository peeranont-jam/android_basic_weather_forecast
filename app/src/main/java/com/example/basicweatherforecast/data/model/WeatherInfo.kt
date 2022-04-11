package com.example.basicweatherforecast.data.model

data class WeatherInfo(
    var lat: Double,
    var long: Double,
    var current: Current,
    var hourly: List<Hourly>,
    var cityName: String? = null,
    var unit: TemperatureUnit? = null
)

data class Current(
    var temp: Double,
    var humidity: Double
)

data class Hourly(
    var temp: Double,
    var humidity: Double,
    var weather: List<Weather>
)

data class Weather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)