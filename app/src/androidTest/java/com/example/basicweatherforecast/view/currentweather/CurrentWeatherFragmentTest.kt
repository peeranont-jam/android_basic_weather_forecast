package com.example.basicweatherforecast.view.currentweather

import android.content.Context
import android.os.SystemClock
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.basicweatherforecast.R
import com.google.common.truth.Truth.assertThat
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CurrentWeatherFragmentTest {

    private lateinit var context: Context

    private lateinit var scenario: FragmentScenario<CurrentWeatherFragment>

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_BasicWeatherForecast)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @Test
    fun show_weather_info_correctly() {
        onView(withId(R.id.tv_city_name)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_temp)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_temp_unit)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_humidity)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_navigate_to_whole_day_forecast)).check(matches(not(isDisplayed())))

        onView(withId(R.id.et_city_name)).perform(clearText(), typeText("san fran"))
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.radio_celsius)).check(matches(isChecked()))
        onView(withId(R.id.radio_fahrenheit)).check(matches(isNotChecked()))

        onView(withId(R.id.btn_search))
            .perform(click())
            .check(matches(isDisplayed()))

        SystemClock.sleep(1000)

        onView(withId(R.id.tv_city_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_temp)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_temp_unit))
            .check(matches(isDisplayed()))
            .check(matches(withText(context.getString(R.string.text_symbol_celsius))))
        onView(withId(R.id.tv_humidity)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_navigate_to_whole_day_forecast)).check(matches(isDisplayed()))
    }

    @Test
    fun select_fahrenheit_radio_button() {
        onView(withId(R.id.tv_city_name)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_temp)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_temp_unit)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_humidity)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_navigate_to_whole_day_forecast)).check(matches(not(isDisplayed())))

        onView(withId(R.id.et_city_name)).perform(clearText(), typeText("bangkok"))
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.radio_fahrenheit)).perform(click())
        onView(withId(R.id.radio_celsius)).check(matches(isNotChecked()))
        onView(withId(R.id.radio_fahrenheit)).check(matches(isChecked()))

        onView(withId(R.id.btn_search))
            .perform(click())
            .check(matches(isDisplayed()))

        SystemClock.sleep(1000)

        onView(withId(R.id.tv_city_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_temp)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_temp_unit))
            .check(matches(isDisplayed()))
            .check(matches(withText(context.getString(R.string.text_symbol_fahrenheit))))
        onView(withId(R.id.tv_humidity)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_navigate_to_whole_day_forecast)).check(matches(isDisplayed()))
    }

    @Test
    fun navigate_to_whole_day_forecast_fragment() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        scenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.et_city_name)).perform(clearText(), typeText("bangkok"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btn_search)).perform(click())
        SystemClock.sleep(1000)

        // Verify that performing a click changes the NavController’s state
        onView(withId(R.id.tv_navigate_to_whole_day_forecast)).perform(click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.wholeDayForecastFragment)
    }

}