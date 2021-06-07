package com.app.weatherforecastapp.repository

import com.app.weatherforecastapp.network.WeatherApi
import com.app.weatherforecastapp.repository.callback.RemoteSourceCallback
import com.app.weatherforecastapp.utils.DataState
import com.app.weatherforecastapp.utils.ResultData
import kotlinx.coroutines.flow.flow

/**
 * This is weather repo helps in calling the apis from the server
 * @param apiService this is api service interface
 */
open class WeatherRepo (private val apiService: WeatherApi) : RemoteSourceCallback {

    /**
     * This is the method for fetching the current weather api
     * from the server
     *
     * @param lat latitude
     * @param lon longitude
     * @return flow data
     */

    override suspend fun fetchWeatherApi(lat: String, lon: String) = flow {
        val response = apiService.getCurrentWeather(lat, lon)
        if (response.isSuccessful) {
            emit(ResultData(response.body()!!, DataState.SUCCESS, response.code(), null))
        } else {
            emit(ResultData(null, DataState.ERROR, response.code(), response.message()))
        }
    }
}