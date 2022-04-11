package com.example.basicweatherforecast.view.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.LiveDataWrapper
import com.example.basicweatherforecast.data.model.TemperatureUnit
import com.example.basicweatherforecast.data.model.WeatherInfo
import com.example.basicweatherforecast.databinding.FragmentCurrentWeatherBinding
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CurrentWeatherFragment : Fragment() {

    private val currentWeatherViewModel: CurrentWeatherViewModel by viewModel {
        parametersOf(Dispatchers.IO)
    }

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.btnSearch.setOnClickListener {
            currentWeatherViewModel.getWeatherInfo(
                binding.etCityName.text.toString(),
                getSelectedTempUnit()
            )
        }

        binding.tvNavigateToWholeDayForecast.setOnClickListener {
            findNavController().navigate(
                CurrentWeatherFragmentDirections.actionCurrentWeatherFragmentToWholeDayForecastFragment(
                    currentWeatherViewModel.getHourlyInfo().toTypedArray(),
                    currentWeatherViewModel.getTempUnit()
                )
            )
        }
    }

    private fun getSelectedTempUnit(): TemperatureUnit {
        return when (binding.radioGroupTempUnit.checkedRadioButtonId) {
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
        binding.tvTempUnit.text = textUnit
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
                    binding.groupWeatherInfo.visibility = View.VISIBLE
                    binding.tvCityName.text = it.cityName ?: ""
                    binding.tvTemp.text = resources.getString(R.string.text_temp, it.current.temp)
                    binding.tvHumidity.text =
                        resources.getString(R.string.text_humidity, it.current.humidity)
                    it.unit?.let { unit -> setTempUnit(unit) }
                } ?: run {
                    binding.groupWeatherInfo.visibility = View.GONE
                }
            }
        }
    }
}