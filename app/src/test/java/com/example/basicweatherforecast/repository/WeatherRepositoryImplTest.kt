package com.example.basicweatherforecast.repository

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.*
import com.example.basicweatherforecast.data.remote.model.*
import com.example.basicweatherforecast.data.remote.service.OpenWeatherMapService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WeatherRepositoryImplTest {

    lateinit var mWeatherRepository: WeatherRepository

    @RelaxedMockK
    lateinit var mOpenWeatherMapService: OpenWeatherMapService

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mWeatherRepository = WeatherRepositoryImpl(mOpenWeatherMapService)
    }


    @Test
    fun `Get geolocation - Should success`() {
        val inputCity = "Bangkok"
        val slot = slot<String>()
        coEvery { mOpenWeatherMapService.getGeolocation(capture(slot)) }.coAnswers {
            listOf(
                GeolocationResponse(inputCity, 13.7544238, 100.4930399, "TH")
            )
        }

        var result: Result<List<Geolocation>>
        runBlocking {
            result = mWeatherRepository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(
            listOf(
                Geolocation(inputCity, 13.7544238, 100.4930399, "TH")
            ), result.data
        )
    }

    @Test
    fun `Get geolocation - Should success with empty array because of city not found`() {
        val inputCity = "z"
        val slot = slot<String>()
        coEvery { mOpenWeatherMapService.getGeolocation(capture(slot)) }.coAnswers {
            listOf()
        }

        var result: Result<List<Geolocation>>
        runBlocking {
            result = mWeatherRepository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(listOf(), result.data)
    }

    @Test
    fun `Get geolocation - Should error with Exception`() {
        val inputCity = "test"
        val errorMessage = "Data not found"
        val slot = slot<String>()
        coEvery { mOpenWeatherMapService.getGeolocation(capture(slot)) }.throws(
            NullPointerException(errorMessage)
        )

        var result: Result<List<Geolocation>>
        runBlocking {
            result = mWeatherRepository.getGeolocation(inputCity)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getGeolocation(any()) }
        assertEquals(inputCity, slot.captured)
        assertFalse(result.isSuccess)
        assertNull(result.data)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun `Get weather info - Should success`() {
        val inputLat = 13.7544238
        val inputLong = 100.4930399
        val inputUnit = "metric"
        val slot = slot<String>()
        val slotList = mutableListOf<Double>()
        coEvery {
            mOpenWeatherMapService.getWeatherInfo(
                capture(slotList),
                capture(slotList),
                capture(slot)
            )
        }.coAnswers {
            WeatherInfoResponse(
                lat = inputLat,
                long = inputLong,
                current = CurrentModel(32.76, 56.0),
                hourly = listOf(
                    HourlyModel(
                        1649703600, 28.5, 50.0,
                        listOf(
                            WeatherModel(
                                500,
                                "Rain",
                                "light rain",
                                "10n"
                            )
                        )
                    )
                )
            )
        }

        var result: Result<WeatherInfo>
        runBlocking {
            result = mWeatherRepository.getWeatherInfo(inputLat, inputLong, inputUnit)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getWeatherInfo(any(), any(), any()) }
        assertEquals(inputLat, slotList[0])
        assertEquals(inputLong, slotList[1])
        assertEquals(inputUnit, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(
            WeatherInfo(
                lat = inputLat,
                long = inputLong,
                current = Current(32.76, 56.0),
                hourly = listOf(
                    Hourly(
                        1649703600, 28.5, 50.0,
                        listOf(
                            Weather(
                                500,
                                "Rain",
                                "light rain",
                                "10n"
                            )
                        )
                    )
                ),
                geolocation = null
            ), result.data
        )
    }

    @Test
    fun `Get weather info - Should error with Exception`() {
        val inputLat = 13.7544238
        val inputLong = 100.4930399
        val inputUnit = "xxx"
        val errorMessage = "Invalid Input"
        coEvery { mOpenWeatherMapService.getWeatherInfo(any(), any(), any()) }.throws(
            IOException(errorMessage)
        )

        var result: Result<WeatherInfo>
        runBlocking {
            result = mWeatherRepository.getWeatherInfo(inputLat, inputLong, inputUnit)
        }

        coVerify(exactly = 1) { mOpenWeatherMapService.getWeatherInfo(any(), any(), any()) }
        assertFalse(result.isSuccess)
        assertNull(result.data)
        assertEquals(errorMessage, result.message)
    }
}