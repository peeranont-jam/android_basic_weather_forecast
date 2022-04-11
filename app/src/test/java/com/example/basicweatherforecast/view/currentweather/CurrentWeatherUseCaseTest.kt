package com.example.basicweatherforecast.view.currentweather

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.*
import com.example.basicweatherforecast.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CurrentWeatherUseCaseTest {

    private lateinit var mCurrentWeatherUseCase: CurrentWeatherUseCase

    @RelaxedMockK
    lateinit var mWeatherRepository: WeatherRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mCurrentWeatherUseCase = CurrentWeatherUseCase(mWeatherRepository)
    }


    @Test
    fun `Get geolocation - Should success`() {
        val input = "Bangkok"
        val slot = slot<String>()
        coEvery { mWeatherRepository.getGeolocation(capture(slot)) } coAnswers {
            Result.success(
                listOf(
                    Geolocation(input, 13.7544238, 100.4930399, "TH")
                )
            )
        }

        val result: Result<List<Geolocation>>
        runBlocking {
            result = mCurrentWeatherUseCase.getGeolocation(input)
        }

        coVerify(exactly = 1) { mWeatherRepository.getGeolocation(any()) }
        assertEquals(input, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(
            listOf(
                Geolocation(input, 13.7544238, 100.4930399, "TH")
            ),
            result.data
        )
        assertNull(result.message)
    }

    @Test
    fun `Get geolocation - Should fail because of Exception`() {
        val input = "Bangkok"
        val errorMsg = "Mock error msg."
        val slot = slot<String>()
        coEvery { mWeatherRepository.getGeolocation(capture(slot)) } coAnswers {
            Result.error(errorMsg)
        }

        val result: Result<List<Geolocation>>
        runBlocking {
            result = mCurrentWeatherUseCase.getGeolocation(input)
        }

        coVerify(exactly = 1) { mWeatherRepository.getGeolocation(any()) }
        assertEquals(input, slot.captured)
        assertFalse(result.isSuccess)
        assertEquals(errorMsg, result.message)
        assertNull(result.data)
    }

    @Test
    fun `Get weather info - Should success`() {
        val inputLat = 13.7544238
        val inputLong = 100.4930399
        val inputUnit = "metric"
        val slot = slot<String>()
        val slotList = mutableListOf<Double>()
        coEvery {
            mWeatherRepository.getWeatherInfo(
                capture(slotList),
                capture(slotList),
                capture(slot)
            )
        } coAnswers {
            Result.success(
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
                    )
                )
            )
        }

        val result: Result<WeatherInfo>
        runBlocking {
            result = mCurrentWeatherUseCase.getWeatherInfo(inputLat, inputLong, inputUnit)
        }

        coVerify(exactly = 1) { mWeatherRepository.getWeatherInfo(any(), any(), any()) }
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
                cityName = null
            ),
            result.data
        )
        assertNull(result.message)
    }

    @Test
    fun `Get weather info - Should fail because of Exception`() {
        val inputLat = 13.7544238
        val inputLong = 100.4930399
        val inputUnit = "test"
        val errorMsg = "Invalid input"
        coEvery {
            mWeatherRepository.getWeatherInfo(any(), any(), any())
        } coAnswers {
            Result.error(errorMsg)
        }

        val result: Result<WeatherInfo>
        runBlocking {
            result = mCurrentWeatherUseCase.getWeatherInfo(inputLat, inputLong, inputUnit)
        }

        coVerify(exactly = 1) { mWeatherRepository.getWeatherInfo(any(), any(), any()) }
        assertFalse(result.isSuccess)
        assertEquals(errorMsg, result.message)
        assertNull(result.data)
    }
}