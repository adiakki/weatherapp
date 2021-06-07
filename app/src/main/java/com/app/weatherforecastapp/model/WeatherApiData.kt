package com.app.weatherforecastapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherApiData (
    @SerializedName("name")
    var name: String = "",

  @SerializedName("main")
  var mainData: MainData? = null,

    @SerializedName("weather")
    var weather: ArrayList<WeatherData>? = null

) : Parcelable