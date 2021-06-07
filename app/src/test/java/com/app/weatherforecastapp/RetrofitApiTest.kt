package com.app.weatherforecastapp

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.weatherforecastapp.model.WeatherApiData
import com.app.weatherforecastapp.network.RetrofitInstance
import com.app.weatherforecastapp.network.WeatherApi
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RetrofitApiTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    @Test
    fun test_weather_api_callback()  {

        val weatherApiService = RetrofitInstance.create(ApplicationProvider.getApplicationContext()).create(WeatherApi::class.java)
        var currentWeatherResponse : Response<WeatherApiData>? = null
        testScope.launch {
            currentWeatherResponse = weatherApiService.getCurrentWeather("27.88", "77.88")
            assertTrue(currentWeatherResponse?.isSuccessful?:false)
        }
    }
}