package com.app.weatherforecastapp.mvvm

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.weatherforecastapp.WeatherApplication
import com.app.weatherforecastapp.callback.LocationCallbackContract
import com.app.weatherforecastapp.manager.LocationManagerImpl
import com.app.weatherforecastapp.mvvm.helper.WeatherViewModelHelper
import com.app.weatherforecastapp.network.WeatherApi
import com.app.weatherforecastapp.repository.WeatherRepo
import com.app.weatherforecastapp.utils.AppUtils
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class WeatherForecastViewModelTest {


    private lateinit var viewModel : WeatherForecastViewModel
    private val appContext = ApplicationProvider.getApplicationContext<WeatherApplication>()

    private lateinit var repo : WeatherRepo

    private lateinit var locationListener: LocationCallbackContract

    private lateinit var locationImpl : LocationManagerImpl

    private lateinit var helper: WeatherViewModelHelper

    @Before
    fun setUp() {
        val apiService = Mockito.spy(WeatherApi::class.java)
        repo = Mockito.spy(WeatherRepo(apiService))
        helper = Mockito.spy(WeatherViewModelHelper())
        viewModel = WeatherForecastViewModel(appContext, repo, helper)
        locationListener = Mockito.mock(LocationCallbackContract::class.java)
        locationImpl = LocationManagerImpl(appContext, locationListener)
    }


    @Test
    fun test_location_permission() {
        Mockito.`when`(helper.checkLocationAccessPermission(appContext)).thenAnswer {false}
        viewModel.getLastLocation()
        assertEquals(AppUtils.REQ_FOR_LOCATION_PERMISSION, viewModel.actionLiveData.value!!.actionCode)
    }

    @After
    fun tearDown() {
    }
}