package com.example.basicweatherforecast.view.currentweather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.LiveDataWrapper
import com.example.basicweatherforecast.data.model.Hourly
import com.example.basicweatherforecast.data.model.TemperatureUnit
import com.example.basicweatherforecast.data.model.WeatherInfo
import kotlinx.coroutines.*

class CurrentWeatherViewModel(
    application: Application,
    private val useCase: CurrentWeatherUseCase,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AndroidViewModel(application) {

    var weatherInfoLiveData = MutableLiveData<LiveDataWrapper<WeatherInfo>>()

    private var hourlyInfo: List<Hourly> = listOf()
    private var temperatureUnit = TemperatureUnit.Celsius

    private val job = SupervisorJob()
    private val mioScope = CoroutineScope(job + ioDispatcher)

    fun getHourlyInfo(): List<Hourly> {
        return hourlyInfo
    }

    fun getTempUnit(): TemperatureUnit {
        return temperatureUnit
    }

    fun getWeatherInfo(cityName: String, unit: TemperatureUnit) {

        if (cityName.isBlank()) {
            weatherInfoLiveData.value =
                LiveDataWrapper.error(getApplication<Application>().getString(R.string.text_err_msg_blank_input))
            return
        }

        mioScope.launch {
            weatherInfoLiveData.postValue(LiveDataWrapper.loading())
            try {
                val geolocation = useCase.getGeolocation(cityName.lowercase())
                if (geolocation.isSuccess
                    && geolocation.data != null
                    && geolocation.data.isNotEmpty()
                ) {
                    val result = useCase.getWeatherInfo(
                        geolocation.data[0].lat, geolocation.data[0].lon, unit.unit
                    )
                    if (result.isSuccess && result.data != null) {
                        result.data.geolocation = geolocation.data[0]
                        result.data.unit = unit
                        weatherInfoLiveData.postValue(LiveDataWrapper.success(result.data))

                        hourlyInfo = result.data.hourly
                        temperatureUnit = unit
                    } else {
                        weatherInfoLiveData.postValue(
                            LiveDataWrapper.error(
                                errMsg = result.message ?: "Weather Info Not Found"
                            )
                        )
                    }
                } else {
                    weatherInfoLiveData.postValue(
                        LiveDataWrapper.error(
                            errMsg = geolocation.message ?: "Geolocation Not Found"
                        )
                    )
                }
            } catch (e: Exception) {
                weatherInfoLiveData.postValue(
                    LiveDataWrapper.error(
                        errMsg = e.message ?: "Unexpected Error"
                    )
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}