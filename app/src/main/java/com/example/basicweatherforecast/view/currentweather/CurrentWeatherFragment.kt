package com.example.basicweatherforecast.view.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.LiveDataWrapper
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

        currentWeatherViewModel.weatherInfoLiveData.observe(viewLifecycleOwner) { result ->
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
                    } ?: run {
                        group_weather_info.visibility = View.GONE
                    }
                }
            }
        }

        btn_search.setOnClickListener {
            currentWeatherViewModel.getWeatherInfo(et_city_name.text.toString(), "metric")
        }
    }
}