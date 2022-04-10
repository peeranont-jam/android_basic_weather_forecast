package com.example.basicweatherforecast.view.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.LiveDataWrapper
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentWeatherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val currentWeatherViewModel: CurrentWeatherViewModel by viewModel {
        parametersOf(
            Dispatchers.IO
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
                    // Error
                }
                LiveDataWrapper.ResponseStatus.SUCCESS -> {
                    tv_info.text = resources.getString(
                        R.string.text_lat_long,
                        result.response?.get(0)?.lat,
                        result.response?.get(0)?.lon
                    )
                    tv_info.visibility = View.VISIBLE
                }
            }
        }

        btn_search.setOnClickListener {
            currentWeatherViewModel.getGeolocation("Tokyo")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrentWeatherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CurrentWeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}