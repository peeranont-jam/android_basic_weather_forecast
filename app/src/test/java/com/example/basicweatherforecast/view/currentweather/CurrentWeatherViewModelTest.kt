package com.example.basicweatherforecast.view.currentweather

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.LiveDataWrapper
import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.*
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class CurrentWeatherViewModelTest {

    @Rule
    @JvmField
    var taskExecutorRule = InstantTaskExecutorRule()

    lateinit var mViewModel: CurrentWeatherViewModel

    @RelaxedMockK
    lateinit var mUseCase: CurrentWeatherUseCase

    @RelaxedMockK
    lateinit var application: Application

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = CurrentWeatherViewModel(application, mUseCase, testDispatcher)
    }

    @Test
    fun `Get weather info - Should success`() {
        val inputCity = "Bangko"
        val mockLat = 13.7544238
        val mockLong = 100.4930399
        val inputUnit = TemperatureUnit.Celsius
        val slotInputCity = slot<String>()
        val slotUnit = slot<String>()
        val slotCityName = slot<String>()
        val slotList = mutableListOf<Double>()

        coEvery { mUseCase.getGeolocation(capture(slotInputCity)) } coAnswers {
            Result.success(
                listOf(
                    Geolocation("Bangkok", mockLat, mockLong, "TH")
                )
            ).also {
                slotCityName.captured = it.data!![0].name
            }
        }

        coEvery {
            mUseCase.getWeatherInfo(
                capture(slotList),
                capture(slotList),
                capture(slotUnit)
            )
        } coAnswers {
            Result.success(
                WeatherInfo(
                    lat = mockLat,
                    long = mockLong,
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

        mViewModel.getWeatherInfo(inputCity, inputUnit)
        coVerify(exactly = 1) { mUseCase.getGeolocation(any()) }
        coVerify(exactly = 1) { mUseCase.getWeatherInfo(any(), any(), any()) }
        assertEquals(mockLat, slotList[0], 0.0)
        assertEquals(mockLong, slotList[1], 0.0)
        assertEquals(inputCity.lowercase(), slotInputCity.captured)
        assertEquals(inputUnit.unit, slotUnit.captured)
        assertEquals(
            LiveDataWrapper.ResponseStatus.SUCCESS,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(
            WeatherInfo(
                lat = mockLat,
                long = mockLong,
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
                cityName = slotCityName.captured,
                unit = inputUnit
            ),
            mViewModel.weatherInfoLiveData.value?.response
        )
    }

    @Test
    fun `Get weather info - Should fail because of get geolocation returns error`() {
        val inputCity = "Bangkok"
        val inputUnit = TemperatureUnit.Celsius
        val errMsg = "Invalid input"
        coEvery { mUseCase.getGeolocation(any()) } coAnswers {
            Result.error(errMsg)
        }

        mViewModel.getWeatherInfo(inputCity, inputUnit)
        coVerify(exactly = 1) { mUseCase.getGeolocation(any()) }
        coVerify(exactly = 0) { mUseCase.getWeatherInfo(any(), any(), any()) }
        assertNull(mViewModel.weatherInfoLiveData.value?.response)
        assertEquals(
            LiveDataWrapper.ResponseStatus.ERROR,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(errMsg, mViewModel.weatherInfoLiveData.value?.errorMessage)
    }

    @Test
    fun `Get weather info - Should fail because of get weather returns error`() {
        val inputCity = "Bangkok"
        val inputUnit = TemperatureUnit.Celsius
        val errMsg = "Invalid input"
        coEvery { mUseCase.getGeolocation(any()) } coAnswers {
            Result.success(
                listOf(
                    Geolocation(inputCity, 10.05, -5.6215, "TH")
                )
            )
        }
        coEvery { mUseCase.getWeatherInfo(any(), any(), any()) } coAnswers {
            Result.error(errMsg)
        }

        mViewModel.getWeatherInfo(inputCity, inputUnit)
        coVerify(exactly = 1) { mUseCase.getGeolocation(any()) }
        coVerify(exactly = 1) { mUseCase.getWeatherInfo(any(), any(), any()) }
        assertNull(mViewModel.weatherInfoLiveData.value?.response)
        assertEquals(
            LiveDataWrapper.ResponseStatus.ERROR,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(errMsg, mViewModel.weatherInfoLiveData.value?.errorMessage)
    }

    @Test
    fun `Get weather info - Should fail because of get geolocation throws exception`() {
        val inputCity = "Bangkok"
        val inputUnit = TemperatureUnit.Celsius
        val errMsg = "No Data"
        coEvery { mUseCase.getGeolocation(any()) } throws NullPointerException(errMsg)

        mViewModel.getWeatherInfo(inputCity, inputUnit)
        coVerify(exactly = 1) { mUseCase.getGeolocation(any()) }
        coVerify(exactly = 0) { mUseCase.getWeatherInfo(any(), any(), any()) }
        assertNull(mViewModel.weatherInfoLiveData.value?.response)
        assertEquals(
            LiveDataWrapper.ResponseStatus.ERROR,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(errMsg, mViewModel.weatherInfoLiveData.value?.errorMessage)
    }

    @Test
    fun `Get weather info - Should fail because blank city name`() {
        val inputCity = ""
        val inputUnit = TemperatureUnit.Celsius
        val errMsg = "Please specify city name!!!"

        every {
            application.getString(R.string.text_err_msg_blank_input)
        } returns errMsg

        mViewModel.getWeatherInfo(inputCity, inputUnit)
        coVerify(exactly = 0) { mUseCase.getGeolocation(any()) }
        coVerify(exactly = 0) { mUseCase.getWeatherInfo(any(), any(), any()) }
        assertNull(mViewModel.weatherInfoLiveData.value?.response)
        assertEquals(
            LiveDataWrapper.ResponseStatus.ERROR,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(errMsg, mViewModel.weatherInfoLiveData.value?.errorMessage)
    }
}