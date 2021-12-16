package com.burlakov.week1application

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.burlakov.week1application.activities.MenuActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val username = "user"
    private val searchText = "cats"

    @get:Rule
    var activityRule: ActivityScenarioRule<MenuActivity> =
        ActivityScenarioRule(MenuActivity::class.java)

    @Before
    fun logOut() {
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_logout))
    }

    @Test
    fun logInTestBadUsername() {
        onView(withId(R.id.usernameEditText)).perform(typeText("    "))
        onView(withId(R.id.singIn)).perform(click())
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun logInTestGoodUsername() {
        onView(withId(R.id.usernameEditText)).perform(typeText(username))
        onView(withId(R.id.singIn)).perform(click())
        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }

    @Test
    fun searchCheck() {
        logInTestGoodUsername()
        onView(withId(R.id.editTextName)).perform(typeText(searchText))
        onView(withId(R.id.search)).perform(click())

        Thread.sleep(2500)

        onView(withId(R.id.recyclerView)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(12)
        )
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText(searchText))))

    }

    @Test
    fun favoritesCheck() {
        searchCheck()

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(12, click()))

        onView(withId(R.id.favorite)).perform(click())
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_favorite))

        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText(searchText))))

    }
}
