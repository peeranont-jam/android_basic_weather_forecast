package com.example.basicweatherforecast.repository

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.data.model.WeatherInfo
import com.example.basicweatherforecast.data.remote.service.OpenWeatherMapService

class WeatherRepositoryImpl(
    private val openWeatherMapService: OpenWeatherMapService
) : WeatherRepository {

    override suspend fun getGeolocation(city: String): Result<List<Geolocation>> {
        return try {
            val response = openWeatherMapService.getGeolocation(city)
            val result = response.map {
                it.mapToGeolocation()
            }
            Result.success(result)
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.error(e.message)
        }
    }

    override suspend fun getWeatherInfo(
        lat: Double,
        long: Double,
        unit: String
    ): Result<WeatherInfo> {
        return try {
            val response = openWeatherMapService.getWeatherInfo(lat, long, unit)
            return Result.success(response.mapToWeatherInfo())
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.error(e.message)
        }
    }
}