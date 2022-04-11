package com.example.basicweatherforecast.di

import com.example.basicweatherforecast.repository.WeatherRepository
import com.example.basicweatherforecast.repository.WeatherRepositoryImpl
import com.example.basicweatherforecast.view.currentweather.CurrentWeatherUseCase
import com.example.basicweatherforecast.view.currentweather.CurrentWeatherViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /* Repository */
    factory<WeatherRepository> { WeatherRepositoryImpl(get()) }

    /* UseCase */
    factory { CurrentWeatherUseCase(get()) }

    /* ViewModel */
    viewModel { parameters ->
        CurrentWeatherViewModel(
            androidApplication(),
            useCase = get(),
            ioDispatcher = parameters.get()
        )
    }
}
