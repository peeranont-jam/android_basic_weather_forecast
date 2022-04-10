package com.example.basicweatherforecast.view.currentweather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicweatherforecast.data.LiveDataWrapper
import com.example.basicweatherforecast.data.model.Geolocation
import kotlinx.coroutines.*

class CurrentWeatherViewModel(
    private val useCase: CurrentWeatherUseCase,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    var weatherInfoLiveData = MutableLiveData<LiveDataWrapper<List<Geolocation>>>()

    private val job = SupervisorJob()
    private val mioScope = CoroutineScope(job + ioDispatcher)

    fun getGeolocation(cityName: String) {
        mioScope.launch {
            weatherInfoLiveData.postValue(LiveDataWrapper.loading())
            try {
                val result = useCase.getGeolocation(cityName)
                if (result.isSuccess) {
                    result.data?.let {
                        weatherInfoLiveData.postValue(LiveDataWrapper.success(it))
                    }
                } else {
                    weatherInfoLiveData.postValue(LiveDataWrapper.error(errMsg = result.message))
                }
            } catch (e: Exception) {
                weatherInfoLiveData.postValue(
                    LiveDataWrapper.error(
                        e.message ?: "Unexpected Error"
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