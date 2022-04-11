package com.example.basicweatherforecast.view.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.LiveDataWrapper
import com.example.basicweatherforecast.data.model.TemperatureUnit
import com.example.basicweatherforecast.data.model.WeatherInfo
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CurrentWeatherFragment : Fragment() {

    private val currentWeatherViewModel: CurrentWeatherViewModel by viewModel {
        parametersOf(
            Dispatchers.IO
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentWeatherViewModel.weatherInfoLiveData.observe(
            viewLifecycleOwner,
            mCurrentWeatherDataObserver
        )

        setupViews()
    }

    private fun setupViews() {
        btn_search.setOnClickListener {
            currentWeatherViewModel.getWeatherInfo(
                et_city_name.text.toString(),
                getSelectedTempUnit()
            )
        }
    }

    private fun getSelectedTempUnit(): TemperatureUnit {
        return when (radio_group_temp_unit.checkedRadioButtonId) {
            R.id.radio_celsius -> TemperatureUnit.Celsius
            R.id.radio_fahrenheit -> TemperatureUnit.Fahrenheit
            else -> TemperatureUnit.Celsius
        }
    }

    private fun setTempUnit(unit: TemperatureUnit) {
        val textUnit = when (unit) {
            TemperatureUnit.Celsius -> getString(R.string.text_symbol_celsius)
            TemperatureUnit.Fahrenheit -> getString(R.string.text_symbol_fahrenheit)
        }
        tv_temp_unit.text = textUnit
    }


    private val mCurrentWeatherDataObserver = Observer<LiveDataWrapper<WeatherInfo>> { result ->
        when (result.responseStatus) {
            LiveDataWrapper.ResponseStatus.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.ResponseStatus.ERROR -> {
                Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                result.response?.let {
                    group_weather_info.visibility = View.VISIBLE
                    tv_city_name.text = it.cityName ?: ""
                    tv_temp.text = resources.getString(R.string.text_temp, it.current.temp)
                    tv_humidity.text =
                        resources.getString(R.string.text_humidity, it.current.humidity)
                    it.unit?.let { unit -> setTempUnit(unit) }
                } ?: run {
                    group_weather_info.visibility = View.GONE
                }
            }
        }
    }
}