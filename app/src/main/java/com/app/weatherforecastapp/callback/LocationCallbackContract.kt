package com.app.weatherforecastapp.callback

import android.location.Location

/**
 * This is the Location callback interface
 */
interface LocationCallbackContract {
    /**
     * Called when the current location is
     * fetched
     */
    fun fetchLocationSuccessfully(latlong: Location)

    /**
     * Called when getting error in fetching
     * the current location
     */
    fun errorInFetchingLocation()
}