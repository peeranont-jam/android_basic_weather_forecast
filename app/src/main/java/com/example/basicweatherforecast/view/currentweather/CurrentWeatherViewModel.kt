package com.example.basicweatherforecast.view.currentweather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.LiveDataWrapper
import com.example.basicweatherforecast.data.model.WeatherInfo
import kotlinx.coroutines.*

class CurrentWeatherViewModel(
    application: Application,
    private val useCase: CurrentWeatherUseCase,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AndroidViewModel(application) {

    var weatherInfoLiveData = MutableLiveData<LiveDataWrapper<WeatherInfo>>()

    private val job = SupervisorJob()
    private val mioScope = CoroutineScope(job + ioDispatcher)

    fun getWeatherInfo(cityName: String, inputUnit: String) {

        if (cityName.isBlank()) {
            weatherInfoLiveData.value =
                LiveDataWrapper.error(getApplication<Application>().getString(R.string.tv_err_msg_blank_input))
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
                        geolocation.data[0].lat, geolocation.data[0].lon, inputUnit
                    )
                    if (result.isSuccess && result.data != null) {
                        result.data.cityName = geolocation.data[0].name
                        weatherInfoLiveData.postValue(LiveDataWrapper.success(result.data))
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