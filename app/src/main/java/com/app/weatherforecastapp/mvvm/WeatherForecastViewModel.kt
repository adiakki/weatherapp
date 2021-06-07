package com.app.weatherforecastapp.mvvm

import android.app.Application
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.weatherforecastapp.R
import com.app.weatherforecastapp.callback.LocationCallbackContract
import com.app.weatherforecastapp.manager.LocationManagerImpl
import com.app.weatherforecastapp.model.WeatherApiData
import com.app.weatherforecastapp.mvvm.helper.WeatherViewModelHelper
import com.app.weatherforecastapp.repository.WeatherRepo
import com.app.weatherforecastapp.utils.ActionData
import com.app.weatherforecastapp.utils.AppUtils
import com.app.weatherforecastapp.utils.DataState
import com.app.weatherforecastapp.utils.ResultData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This is ViewModel that helps in fetching the
 * user current location and fetch the current
 * weather data from the server.
 */
open class WeatherForecastViewModel (private val appContext: Application, private val repo: WeatherRepo, private val helper: WeatherViewModelHelper) : AndroidViewModel(appContext),
    LocationCallbackContract {

    /**
     * This is the location manager impl that take care the current location
     * fetching activity
     */
    private val locationManagerImpl = LocationManagerImpl(appContext.applicationContext, this)

    /**
     * This is the live data used for notifying the actions to perform
     */
    var actionLiveData: MutableLiveData<ActionData> = MutableLiveData()

    /**
     * This live data used to provide the weather api data to the view
     */
    var weatherApiLiveData: MutableLiveData<ResultData<WeatherApiData>> = MutableLiveData()

    /***
     * This is the app level shared preference
     */
    private val sharedPreferences : SharedPreferences by lazy {
        AppUtils.getAppSharedPreference(appContext)
    }


    /**
     * This is the method that is called from the
     * view to fetch the current location
     */
    fun getLastLocation() {
        if (helper.checkLocationAccessPermission(appContext)) {
            if (locationManagerImpl.isLocationEnabled()) {
                locationManagerImpl.requestNewLocationData()
            } else {
                actionLiveData.postValue(ActionData(AppUtils.OPEN_SETTING_PAGE))
            }
        } else {
            actionLiveData.value = ActionData(AppUtils.REQ_FOR_LOCATION_PERMISSION)
        }
    }

    /**
     * This is the method that fetch the current weather data
     * on basis of the lat and long of the device.
     *
     * @param lat this is latitude
     * @param lon this is the longitude
     */
     private fun fetchWeatherForecastData(lat: Double, lon: Double)  {
        weatherApiLiveData.postValue(ResultData(null, DataState.LOADING, -1, null))
        viewModelScope.launch {
            repo.fetchWeatherApi(lat.toString(), lon.toString())
                .collect {
                    weatherApiLiveData.postValue(ResultData(it.data, it.dataState, it.errorCode, it.msg))
                }
        }
    }



    /**
     * This is the callback called when location
     * successfully fetched.
     * @param latlong It is the @Location object having latitude and longitude
     */
    override fun fetchLocationSuccessfully(latlong: Location) {
        sharedPreferences.edit().putString(appContext.getString(R.string.lat), latlong.latitude.toString()).putString(appContext.getString(R.string.lon), latlong.longitude.toString()).apply()
        val hasDataStoredOnce = sharedPreferences.getBoolean(appContext.getString(R.string.key_is_data_stored), false)

        if (!hasDataStoredOnce) {
            if (AppUtils.isInternetAvailable(appContext)) {
                fetchWeatherForecastData(latlong.latitude, latlong.longitude)
            } else {
                // show no internet connection popup and retry
                actionLiveData.postValue(ActionData(AppUtils.SHOW_NO_NETWORK_CONNECTION_POP_UP))
            }
        } else {
            fetchWeatherForecastData(latlong.latitude, latlong.longitude)
        }
    }

    /**
     * This is the method called when getting some issue
     * in fetching the current location
     */
    override fun errorInFetchingLocation() {

    }
}