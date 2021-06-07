package com.app.weatherforecastapp.mvvm.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.weatherforecastapp.mvvm.WeatherForecastViewModel
import com.app.weatherforecastapp.mvvm.helper.WeatherViewModelHelper
import com.app.weatherforecastapp.network.RetrofitInstance
import com.app.weatherforecastapp.network.WeatherApi
import com.app.weatherforecastapp.repository.WeatherRepo

/**
 * This is the ViewModel factory class used to create the
 * WeatherViewModel instance
 */
class WeatherViewModelFactory(private val context: Application) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherForecastViewModel(context, WeatherRepo(RetrofitInstance.create(context).create(WeatherApi::class.java)), WeatherViewModelHelper()) as T
    }
}