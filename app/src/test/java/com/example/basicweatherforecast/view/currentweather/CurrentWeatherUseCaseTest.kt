package com.example.basicweatherforecast.view.currentweather

import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import com.example.basicweatherforecast.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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
}