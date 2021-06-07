package com.app.weatherforecastapp.network

import com.app.weatherforecastapp.model.WeatherApiData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This is the api service where
 * we define the api end points
 */
interface WeatherApi {

    @GET("weather?appid=b72fc1c72772ca1a8754be92cdd941d4")
    suspend fun getCurrentWeather(@Query("lat") lat: String, @Query("lon") lon: String) : Response<WeatherApiData>
}