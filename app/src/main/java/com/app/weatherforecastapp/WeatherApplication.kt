package com.app.weatherforecastapp


import android.app.Application
import androidx.work.*
import com.app.weatherforecastapp.workmanager.PeriodicWorkerThread
import java.util.concurrent.TimeUnit

class WeatherApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        schedulePeriodicApiCall()
    }

    /**
     * Schedule the work manager for calling
     * weather api after every 2 hours
     */
    private fun schedulePeriodicApiCall() {
        // provide custom configuration
        val myConfig = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

        // initialize WorkManager
        WorkManager.initialize(this, myConfig)
        val mWorkManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val periodicBuilder = PeriodicWorkRequest.Builder(PeriodicWorkerThread::class.java, 2, TimeUnit.HOURS).setConstraints(constraints)
        val myWork = periodicBuilder.addTag(PeriodicWorkerThread::class.java.simpleName).build()
        mWorkManager.enqueueUniquePeriodicWork(PeriodicWorkerThread::class.java.simpleName, ExistingPeriodicWorkPolicy.KEEP, myWork)
    }

    override fun getWorkManagerConfiguration(): Configuration {
       return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }
}