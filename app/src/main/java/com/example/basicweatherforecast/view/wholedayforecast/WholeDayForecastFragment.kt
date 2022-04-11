package com.example.basicweatherforecast.view.wholedayforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.view.wholedayforecast.adapter.WholeDayForecastAdapter
import kotlinx.android.synthetic.main.fragment_whole_day_forecast.*


class WholeDayForecastFragment : Fragment() {

    private val args: WholeDayForecastFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_whole_day_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val mAdapter =
            WholeDayForecastAdapter(args.hourlyInfo.take(24).toTypedArray(), args.temperatureUnit)
        rv_forecast.layoutManager = LinearLayoutManager(context)
        rv_forecast.adapter = mAdapter
    }
}