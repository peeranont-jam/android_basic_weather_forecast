package com.example.basicweatherforecast.view.currentweather

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.repository.WeatherRepository

class CurrentWeatherUseCase(private val weatherRepository: WeatherRepository) {

    suspend fun getGeolocation(city: String): Result<List<Geolocation>> {
        return weatherRepository.getGeolocation(city)
    }
}