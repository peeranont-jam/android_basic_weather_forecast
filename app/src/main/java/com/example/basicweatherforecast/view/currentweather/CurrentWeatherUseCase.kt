package com.example.basicweatherforecast.view.currentweather

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.data.model.WeatherInfo
import com.example.basicweatherforecast.repository.WeatherRepository

class CurrentWeatherUseCase(private val weatherRepository: WeatherRepository) {

    suspend fun getGeolocation(city: String): Result<List<Geolocation>> {
        return weatherRepository.getGeolocation(city)
    }

    suspend fun getWeatherInfo(
        lat: Double, long: Double, unit: String
    ): Result<WeatherInfo> {
        return weatherRepository.getWeatherInfo(lat, long, unit)
    }
}