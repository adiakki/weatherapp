package com.app.weatherforecastapp.repository.callback

import com.app.weatherforecastapp.model.WeatherApiData
import com.app.weatherforecastapp.utils.ResultData
import kotlinx.coroutines.flow.Flow

/**
 * This is the remote callback method
 */
interface RemoteSourceCallback {
    suspend fun fetchWeatherApi(lat: String, lon: String) : Flow<ResultData<out WeatherApiData>>
}