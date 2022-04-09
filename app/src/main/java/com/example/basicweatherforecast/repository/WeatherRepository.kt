package com.example.basicweatherforecast.repository

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation

interface WeatherRepository {
    suspend fun getGeolocation(city: String): Result<Geolocation>
}