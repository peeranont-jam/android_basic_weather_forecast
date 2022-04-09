package com.example.basicweatherforecast.repository

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.data.model.GeolocationInfo
import com.example.basicweatherforecast.data.remote.model.GeolocationModel
import com.example.basicweatherforecast.data.remote.model.GeolocationResponse
import com.example.basicweatherforecast.data.remote.service.OpenWeatherMapService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WeatherRepositoryImplTest {

    @Test
    fun `Get geolocation - Should success`() {
        val service = mockk<OpenWeatherMapService>()
        val repository = WeatherRepositoryImpl(service)
        val inputCity = "Bangkok"
        val slot = slot<String>()
        coEvery { service.getGeolocation(capture(slot)) }.coAnswers {
            GeolocationResponse(
                listOf(
                    GeolocationModel(inputCity, 13.7544238, 100.4930399, "TH", inputCity)
                )
            )
        }

        var result: Result<Geolocation>
        runBlocking {
            result = repository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { service.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(
            Geolocation(
                listOf(
                    GeolocationInfo(inputCity, 13.7544238, 100.4930399, "TH", inputCity)
                )
            ), result.data
        )
    }

    @Test
    fun `Get geolocation - Should success with empty array because of city not found`() {
        val service = mockk<OpenWeatherMapService>()
        val repository = WeatherRepositoryImpl(service)
        val inputCity = "z"
        val slot = slot<String>()
        coEvery { service.getGeolocation(capture(slot)) }.coAnswers {
            GeolocationResponse(listOf())
        }

        var result: Result<Geolocation>
        runBlocking {
            result = repository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { service.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(Geolocation(listOf()), result.data)
    }

    @Test
    fun `Get geolocation - Should error with Exception`() {
        val service = mockk<OpenWeatherMapService>()
        val repository = WeatherRepositoryImpl(service)
        val inputCity = "test"
        val errorMessage = "Data not found"
        val slot = slot<String>()
        coEvery { service.getGeolocation(capture(slot)) }.throws(
            NullPointerException(errorMessage)
        )

        var result: Result<Geolocation>
        runBlocking {
            result = repository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { service.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertFalse(result.isSuccess)
        assertNull(result.data)
        assertEquals(errorMessage, result.message)
    }
}