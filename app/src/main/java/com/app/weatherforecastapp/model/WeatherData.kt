package com.app.weatherforecastapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * {
"id": 801,
"main": "Clouds",
"description": "few clouds",
"icon": "02d"
}
 */

@Parcelize
data class WeatherData (
    @SerializedName("description")
    var description: String = ""
) : Parcelable