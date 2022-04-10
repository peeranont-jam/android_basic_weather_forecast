package com.example.basicweatherforecast.view.currentweather

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.data.model.GeolocationInfo
import com.example.basicweatherforecast.repository.WeatherRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CurrentWeatherUseCaseTest {

    lateinit var mCurrentWeatherUseCase: CurrentWeatherUseCase

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
                Geolocation(
                    listOf(
                        GeolocationInfo(input, 13.7544238, 100.4930399, "TH", input)
                    )
                )
            )
        }

        val result: Result<Geolocation>
        runBlocking {
            result = mCurrentWeatherUseCase.getGeolocation(input)
        }

        coVerify(exactly = 1) { mWeatherRepository.getGeolocation(any()) }
        assertEquals(input, slot.captured)
        assertTrue(result.isSuccess)
        assertEquals(
            Geolocation(
                listOf(
                    GeolocationInfo(input, 13.7544238, 100.4930399, "TH", input)
                )
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

        val result: Result<Geolocation>
        runBlocking {
            result = mCurrentWeatherUseCase.getGeolocation(input)
        }

        coVerify(exactly = 1) { mWeatherRepository.getGeolocation(any()) }
        assertEquals(input, slot.captured)
        assertFalse(result.isSuccess)
        assertEquals(errorMsg, result.message)
        assertNull(result.data)
    }
}