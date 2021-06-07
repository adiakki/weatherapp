package com.app.weatherforecastapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.app.weatherforecastapp.mvvm.WeatherForecastViewModel
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.observe
import com.app.weatherforecastapp.R
import com.app.weatherforecastapp.model.WeatherApiData
import com.app.weatherforecastapp.mvvm.factory.WeatherViewModelFactory
import com.app.weatherforecastapp.utils.AppUtils
import com.app.weatherforecastapp.utils.DataState
import kotlinx.android.synthetic.main.activity_weather_forcast_layout.*


class WeatherForecastActivity : AppCompatActivity() {

    val mViewModel : WeatherForecastViewModel? by lazy {
        ViewModelProvider(this, WeatherViewModelFactory(application)).get(WeatherForecastViewModel::class.java)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        AppUtils.getAppSharedPreference(applicationContext)
    }

    companion object {
        private const val PERMISSION_ID = 1
        private const val CONVERT_TO_CENTIGRADE = 273.15
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forcast_layout)


        /**
         * Observe the livedata for performing
         * other operations
         */
        mViewModel?.actionLiveData?.observe(this) {

            when (it.actionCode) {

                AppUtils.OPEN_SETTING_PAGE -> {
                    openYourLocation()
                }

                AppUtils.REQ_FOR_LOCATION_PERMISSION -> {
                    requestForLocationPermission()
                }

                AppUtils.SHOW_NO_NETWORK_CONNECTION_POP_UP -> {
                    showNoInternetConnectionPopup()
                }
            }
        }

        /**
         * Observe the weather api data
         */
        observeWeatherApiData()

        /**
         * fetching the user current location
         */
        mViewModel?.getLastLocation()
    }


    /**
     * This method called when device location
     * are off so need to redirect to the page to turned on the location
     */
    private fun openYourLocation() {
        Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show()
        val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }


    /**
     * Function called for requesting the
     * location fetch permissions
     */
    private fun requestForLocationPermission() {
        ActivityCompat.requestPermissions(
            this@WeatherForecastActivity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    /**
     * This method is the callback of
     * permission result
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mViewModel?.getLastLocation()
            }
        }
    }


    /**
     * This method is for showing the
     * No internet connection popup
     */
    private fun showNoInternetConnectionPopup() {
        Toast.makeText(this@WeatherForecastActivity, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show()
    }

    /**
     * This method is for fetching the weather api
     * data
     */
    private fun observeWeatherApiData() {
        mViewModel?.weatherApiLiveData?.observe(this) {

            when (it.dataState) {
                DataState.LOADING -> {

                }
                DataState.SUCCESS -> {

                }
                DataState.ERROR -> {

                }
            }

            it.data?.let {weatherData ->
                sharedPreferences.edit().putBoolean("isDataStored",true).apply()
                updateUI(weatherData)
            }
        }
    }

    /**
     * This method is used to show the
     * current weather api data on the UI
     */
    private fun updateUI(weatherData: WeatherApiData) {

        if (weatherData.mainData?.currentTemp!!.compareTo(0.0) ==  1) {
            tv_current_temp?.text = getString(R.string.lbl_current_temp, (weatherData.mainData?.currentTemp!! - CONVERT_TO_CENTIGRADE).toInt().toString())
        }

        if (weatherData.mainData?.minTemp!!.compareTo(0.0) ==  1) {
            tv_min_temp?.text = getString(R.string.lbl_min_temp, (weatherData.mainData?.minTemp!! - CONVERT_TO_CENTIGRADE).toInt().toString())
        }

        if (weatherData.mainData?.maxTemp!!.compareTo(0.0) ==  1) {
            tv_max_temp?.text = getString(R.string.lbl_max_temp, (weatherData.mainData?.maxTemp!! - CONVERT_TO_CENTIGRADE).toInt().toString())
        }

        if (weatherData.mainData?.humidity!!.compareTo(0.0) == 1) {
            tv_humidity?.text = getString(R.string.lbl_humidity, weatherData.mainData?.humidity.toString())
        }

        if (weatherData.weather?.isNotEmpty() == true) {
            tv_weather?.text = getString(R.string.lbl_weather, weatherData.weather!![0].description)
        }

        if (!TextUtils.isEmpty(weatherData.name)) {
            tv_location?.text = getString(R.string.lbl_current_location, weatherData.name)
        }
    }

}