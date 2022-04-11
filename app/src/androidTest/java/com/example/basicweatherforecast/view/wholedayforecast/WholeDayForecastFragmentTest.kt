package com.example.basicweatherforecast.view.wholedayforecast

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.basicweatherforecast.R
import com.example.basicweatherforecast.data.model.Hourly
import com.example.basicweatherforecast.data.model.TemperatureUnit
import com.example.basicweatherforecast.data.model.Weather
import com.example.basicweatherforecast.view.wholedayforecast.adapter.WholeDayForecastAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WholeDayForecastFragmentTest {

    private lateinit var context: Context

    private lateinit var scenario: FragmentScenario<WholeDayForecastFragment>

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        val args = WholeDayForecastFragmentArgs(
            hourlyInfo = arrayOf(
                Hourly(
                    1649703600, 28.59, 50.0,
                    listOf(Weather(500, "Rain", "light rain", "10n"))
                ),
                Hourly(
                    1649707200, 29.63, 100.1625,
                    listOf(Weather(500, "Rain", "light rain", "10n"))
                )
            ),
            temperatureUnit = TemperatureUnit.Celsius
        )
        val bundle = args.toBundle()
        scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_BasicWeatherForecast
        )
        scenario.moveToState(newState = Lifecycle.State.STARTED)

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        scenario.onFragment {
            // Just like setGraph(), this needs to be called on the main thread
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.wholeDayForecastFragment, bundle)
        }
    }

    @Test
    fun show_weather_info_correctly() {
        onView(withId(R.id.rv_forecast)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_forecast)).perform(
            RecyclerViewActions.scrollTo<WholeDayForecastAdapter.ViewHolder>(
                hasDescendant(withText("12 Apr 02:00"))
            )
        )
        onView(withText("12 Apr 02:00")).check(matches(isDisplayed()))
        onView(withId(R.id.rv_forecast)).perform(
            RecyclerViewActions.scrollTo<WholeDayForecastAdapter.ViewHolder>(
                hasDescendant(withText("12 Apr 03:00"))
            )
        )
        onView(withText("12 Apr 03:00")).check(matches(isDisplayed()))
    }
}