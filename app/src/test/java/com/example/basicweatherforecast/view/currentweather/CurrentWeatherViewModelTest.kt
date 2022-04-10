package com.example.basicweatherforecast.view.currentweather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.basicweatherforecast.data.LiveDataWrapper
import com.example.basicweatherforecast.data.Result
import com.example.basicweatherforecast.data.model.Geolocation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = CurrentWeatherViewModel(mUseCase, testDispatcher)
    }

    @Test
    fun `Get geolocation - Should success`() {
        val input = "Bangkok"
        coEvery { mUseCase.getGeolocation(any()) } coAnswers {
            Result.success(
                listOf(
                    Geolocation(input, 13.7544238, 100.4930399, "TH")
                )
            )
        }

        mViewModel.getGeolocation(input)
        coVerify(exactly = 1) { mUseCase.getGeolocation(any()) }
        assertEquals(
            LiveDataWrapper.ResponseStatus.SUCCESS,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(
            listOf(
                Geolocation(input, 13.7544238, 100.4930399, "TH")
            ),
            mViewModel.weatherInfoLiveData.value?.response
        )
    }

    @Test
    fun `Get geolocation - Should fail because of Result Fail`() {
        val input = "Bangkok"
        val errMsg = "Invalid input"
        coEvery { mUseCase.getGeolocation(any()) } coAnswers {
            Result.error(errMsg)
        }

        mViewModel.getGeolocation(input)
        coVerify(exactly = 1) { mUseCase.getGeolocation(any()) }
        assertNull(mViewModel.weatherInfoLiveData.value?.response)
        assertEquals(
            LiveDataWrapper.ResponseStatus.ERROR,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(errMsg, mViewModel.weatherInfoLiveData.value?.errorMessage)
    }

    @Test
    fun `Get geolocation - Should fail because of Exception`() {
        val input = "Bangkok"
        val errMsg = "No Data"
        coEvery { mUseCase.getGeolocation(any()) } throws NullPointerException(errMsg)

        mViewModel.getGeolocation(input)
        coVerify(exactly = 1) { mUseCase.getGeolocation(any()) }
        assertNull(mViewModel.weatherInfoLiveData.value?.response)
        assertEquals(
            LiveDataWrapper.ResponseStatus.ERROR,
            mViewModel.weatherInfoLiveData.value?.responseStatus
        )
        assertEquals(errMsg, mViewModel.weatherInfoLiveData.value?.errorMessage)
    }
}