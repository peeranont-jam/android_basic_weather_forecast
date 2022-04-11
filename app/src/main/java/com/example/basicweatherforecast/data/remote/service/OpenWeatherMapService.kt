package com.example.basicweatherforecast.data.remote.service

import com.example.basicweatherforecast.data.local.API_KEY
import com.example.basicweatherforecast.data.remote.model.GeolocationResponse
import com.example.basicweatherforecast.data.remote.model.WeatherInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("/geo/1.0/direct")
    suspend fun getGeolocation(
        @Query("q") city: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = API_KEY,
    ): List<GeolocationResponse>

    @GET("/data/2.5/onecall")
    suspend fun getWeatherInfo(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("exclude") exclude: String = "minutely,daily,alerts",
        @Query("appid") apiKey: String = API_KEY
    ): WeatherInfoResponse
}