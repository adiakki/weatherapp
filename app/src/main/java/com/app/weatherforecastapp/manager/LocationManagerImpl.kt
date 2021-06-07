package com.app.weatherforecastapp.manager

import android.content.Context
import android.location.LocationManager
import com.app.weatherforecastapp.callback.LocationCallbackContract
import android.location.LocationManager.NETWORK_PROVIDER
import android.location.LocationManager.GPS_PROVIDER
import com.google.android.gms.location.*

/**
 * This is class which help in fetching the
 * device current location
 */
class LocationManagerImpl (private val context: Context, private val listener: LocationCallbackContract) {

    private var mFusedLocationClient: FusedLocationProviderClient? = null


    /**
     * This is the method used to
     * check the location is enabled or not
     */
    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return locationManager!!.isProviderEnabled(GPS_PROVIDER) || locationManager.isProviderEnabled(
            NETWORK_PROVIDER
        )
    }

    /**
     * This method is used to raise the request
     * for fetching the current location of the
     * device.
     */
     fun requestNewLocationData() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient?.lastLocation?.addOnCanceledListener {

        }?.addOnSuccessListener {
            if (it == null) {
                listener.errorInFetchingLocation()
            } else {
                listener.fetchLocationSuccessfully(it)
            }
        }?.addOnFailureListener {
            listener.errorInFetchingLocation()
        }
    }
}