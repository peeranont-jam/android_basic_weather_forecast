package com.example.basicweatherforecast.view.currentweather

import android.os.SystemClock
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.basicweatherforecast.R
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrentWeatherFragmentTest {

    private lateinit var scenario: FragmentScenario<CurrentWeatherFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_BasicWeatherForecast)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @Test
    fun show_blank_text() {
        onView(withId(R.id.tv_city_name)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_temp)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_temp_unit)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_humidity)).check(matches(not(isDisplayed())))

        onView(withId(R.id.et_city_name)).perform(clearText(), typeText("san fran"))

        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btn_search))
            .perform(click())
            .check(matches(isDisplayed()))

        SystemClock.sleep(1000)

        onView(withId(R.id.tv_city_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_temp)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_temp_unit)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_humidity)).check(matches(isDisplayed()))
    }
}