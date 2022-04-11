package com.example.basicweatherforecast.repository

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.data.model.WeatherInfo

interface WeatherRepository {
    suspend fun getGeolocation(city: String): Result<List<Geolocation>>
    suspend fun getWeatherInfo(lat: Double, long: Double, unit: String): Result<WeatherInfo>
}