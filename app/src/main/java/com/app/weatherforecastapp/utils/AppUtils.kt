package com.app.weatherforecastapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * This is the App Common utils
 * having network connectivity method
 */
@Suppress("DEPRECATION")
class AppUtils {

    companion object {

        const val OPEN_SETTING_PAGE = 1
        const val REQ_FOR_LOCATION_PERMISSION = 2
        const val SHOW_NO_NETWORK_CONNECTION_POP_UP = 3

        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                if (connectivityManager.activeNetworkInfo != null ) {
                    result = when (connectivityManager.activeNetworkInfo?.type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }

            return result
        }

        /**
         * This method helps in checking the wifi connectivity
         */
        fun isWifiAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    else -> false
                }
            } else {
                if (connectivityManager.activeNetworkInfo != null ) {
                    result = when (connectivityManager.activeNetworkInfo?.type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        else -> false
                    }
                }
            }

            return result
        }

        fun getAppSharedPreference(context: Context) = context.getSharedPreferences("weather_app_pref", Context.MODE_PRIVATE) as SharedPreferences
    }
}

data class ResultData<T> (var data: T?, var dataState: DataState, var errorCode: Int, var msg: String?)
enum class DataState {LOADING, SUCCESS, ERROR}
data class ActionData (var actionCode: Int)