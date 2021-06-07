package com.app.weatherforecastapp

import android.os.Build
import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.weatherforecastapp.activity.WeatherForecastActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class TestWeatherActivity {

    @Test
    fun test_current_weather_visible() {

        val activityScenario = ActivityScenario.launch(WeatherForecastActivity::class.java)

        activityScenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.onActivity { weatherActivity ->

            weatherActivity.mViewModel?.weatherApiLiveData?.observe(weatherActivity) {

                if (it.data != null) {
                    Espresso.onView(withId(R.id.tv_current_temp)).check(
                        ViewAssertions.matches(
                            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                        )
                    )
                }
            }
        }
    }

    @Test
    fun test_current_location_visible() {
        val activityScenario = ActivityScenario.launch(WeatherForecastActivity::class.java)

        activityScenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.onActivity { weatherActivity ->

            weatherActivity.mViewModel?.weatherApiLiveData?.observe(weatherActivity) {
                if (it.data != null && !TextUtils.isEmpty(it.data?.name)) {
                    Espresso.onView(withId(R.id.tv_location)).check(
                        ViewAssertions.matches(
                            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                        )
                    )
                }
            }
        }
    }

    @Test
    fun test_location_fetched() {

        val activityScenario = ActivityScenario.launch(WeatherForecastActivity::class.java)

        activityScenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.onActivity { weatherActivity ->

            weatherActivity.mViewModel?.weatherApiLiveData?.observe(weatherActivity) {

                if (it.data != null) {
                    Espresso.onView(withId(R.id.tv_current_temp)).check(
                        ViewAssertions.matches(
                            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                        )
                    )
                }
            }
        }
    }
}