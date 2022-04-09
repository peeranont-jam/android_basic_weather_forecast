package com.example.basicweatherforecast.data.remote.service

import com.example.basicweatherforecast.data.local.API_KEY
import com.example.basicweatherforecast.data.remote.model.GeolocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("/geo/1.0/direct")
    suspend fun getGeolocation(
        @Query("q") city: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = API_KEY,
    ): GeolocationResponse

}