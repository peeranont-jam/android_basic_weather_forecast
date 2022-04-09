package com.example.basicweatherforecast.repository

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.data.model.GeolocationInfo
import com.example.basicweatherforecast.data.remote.model.GeolocationModel
import com.example.basicweatherforecast.data.remote.model.GeolocationResponse
import com.example.basicweatherforecast.data.remote.service.OpenWeatherMapService
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WeatherRepositoryImplTest {

    lateinit var mWeatherRepository: WeatherRepository

    @RelaxedMockK
    lateinit var mOpenWeatherMapService: OpenWeatherMapService

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        mWeatherRepository = WeatherRepositoryImpl(mOpenWeatherMapService)
    }


    @Test
    fun `Get geolocation - Should success`() {
        val inputCity = "Bangkok"
        val slot = slot<String>()
        coEvery { mOpenWeatherMapService.getGeolocation(capture(slot)) }.coAnswers {
            GeolocationResponse(
                listOf(
                    GeolocationModel(inputCity, 13.7544238, 100.4930399, "TH", inputCity)
                )
            )
        }

        var result: Result<Geolocation>
        runBlocking {
            result = mWeatherRepository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getGeolocation(any()) }
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
        val inputCity = "z"
        val slot = slot<String>()
        coEvery { mOpenWeatherMapService.getGeolocation(capture(slot)) }.coAnswers {
            GeolocationResponse(listOf())
        }

        var result: Result<Geolocation>
        runBlocking {
            result = mWeatherRepository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(Geolocation(listOf()), result.data)
    }

    @Test
    fun `Get geolocation - Should error with Exception`() {
        val inputCity = "test"
        val errorMessage = "Data not found"
        val slot = slot<String>()
        coEvery { mOpenWeatherMapService.getGeolocation(capture(slot)) }.throws(
            NullPointerException(errorMessage)
        )

        var result: Result<Geolocation>
        runBlocking {
            result = mWeatherRepository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertFalse(result.isSuccess)
        assertNull(result.data)
        assertEquals(errorMessage, result.message)
    }
}