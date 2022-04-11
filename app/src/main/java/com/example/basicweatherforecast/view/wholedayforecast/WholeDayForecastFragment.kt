package com.example.basicweatherforecast.view.wholedayforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicweatherforecast.databinding.FragmentWholeDayForecastBinding
import com.example.basicweatherforecast.view.wholedayforecast.adapter.WholeDayForecastAdapter


class WholeDayForecastFragment : Fragment() {

    private val args: WholeDayForecastFragmentArgs by navArgs()

    private var _binding: FragmentWholeDayForecastBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWholeDayForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        val mAdapter =
            WholeDayForecastAdapter(args.hourlyInfo.take(24).toTypedArray(), args.temperatureUnit)
        binding.rvForecast.layoutManager = LinearLayoutManager(context)
        binding.rvForecast.adapter = mAdapter
    }
}