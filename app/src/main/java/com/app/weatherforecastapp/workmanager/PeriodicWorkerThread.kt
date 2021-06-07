package com.app.weatherforecastapp.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.weatherforecastapp.R
import com.app.weatherforecastapp.network.RetrofitInstance
import com.app.weatherforecastapp.network.WeatherApi
import com.app.weatherforecastapp.utils.AppUtils

/**
 * This is the periodic worker thread
 * that calls the weather api after every 2 hour
 */
class PeriodicWorkerThread(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {


    override suspend fun doWork(): Result   {
        if (AppUtils.isWifiAvailable(context)) {
            /**
             * call weather api if and only if wifi is connected.
             */
            val sharedPreferences = AppUtils.getAppSharedPreference(context.applicationContext)
            val response = RetrofitInstance.create(context.applicationContext)
                .create(WeatherApi::class.java).
                getCurrentWeather(sharedPreferences.getString(context.getString(R.string.lat), "") ?: "",
                    sharedPreferences.getString(context.getString(R.string.lon), "") ?: "")
            return if (response.isSuccessful) {
                AppUtils.getAppSharedPreference(context).edit().putBoolean(context.getString(R.string.key_is_data_stored), true).apply()
                Result.success()
            } else {
                Result.retry()
            }
        } else {
            AppUtils.getAppSharedPreference(context).edit().putBoolean(context.getString(R.string.key_is_data_stored), false).apply()
            return Result.retry()
        }
    }
}