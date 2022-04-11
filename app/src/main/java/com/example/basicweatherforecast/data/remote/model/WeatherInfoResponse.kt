package com.example.basicweatherforecast.data.remote.model

import com.example.basicweatherforecast.data.model.Current
import com.example.basicweatherforecast.data.model.Hourly
import com.example.basicweatherforecast.data.model.Weather
import com.example.basicweatherforecast.data.model.WeatherInfo
import com.google.gson.annotations.SerializedName

data class WeatherInfoResponse(
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lon")
    var long: Double,
    @SerializedName("current")
    var current: CurrentModel,
    @SerializedName("hourly")
    var hourly: List<HourlyModel>
) {
    fun mapToWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            lat = this.lat,
            long = this.long,
            current = Current(
                temp = this.current.temp,
                humidity = this.current.humidity
            ),
            hourly = this.hourly.map {
                Hourly(
                    temp = it.temp,
                    humidity = it.humidity,
                    weather = it.weather.map { weather ->
                        Weather(
                            id = weather.id,
                            main = weather.main,
                            description = weather.description,
                            icon = weather.icon
                        )
                    }
                )
            }
        )
    }
}

data class CurrentModel(
    @SerializedName("temp")
    var temp: Double,
    @SerializedName("humidity")
    var humidity: Double
)

data class HourlyModel(
    @SerializedName("temp")
    var temp: Double,
    @SerializedName("humidity")
    var humidity: Double,
    @SerializedName("weather")
    var weather: List<WeatherModel>
)

data class WeatherModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("main")
    var main: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("icon")
    var icon: String
)