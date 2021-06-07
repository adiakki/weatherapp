package com.app.weatherforecastapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/***
 *
 * {
"temp": 308.15,
"feels_like": 309.78,
"temp_min": 308.15,
"temp_max": 308.15,
"pressure": 1005,
"humidity": 38
}
 *
 */

@Parcelize
data class MainData (
    @SerializedName("temp")
    var currentTemp: Double  = 0.0,

    @SerializedName("temp_min")
    var minTemp: Double  = 0.0,

    @SerializedName("temp_max")
    var maxTemp: Double  = 0.0,

    @SerializedName("humidity")
    var humidity: Int?  = 0

) : Parcelable