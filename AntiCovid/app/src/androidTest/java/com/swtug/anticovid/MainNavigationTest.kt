package com.swtug.anticovid

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swtug.anticovid.utils.selectTabAtPosition
import com.swtug.anticovid.view.main.MainFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@RunWith(AndroidJUnit4::class)
class MainNavigationTest {
    private lateinit var navController: NavController

    @Before
    fun setup() {
        navController = mock(NavController::class.java)

        val mainScenario = launchFragmentInContainer<MainFragment>(themeResId = R.style.Theme_AntiCovid)

        mainScenario.withFragment {
            Navigation.setViewNavController(requireView(), navController)
        }
    }



    @Test
    fun testNavigateFromHomeToTestResults() {
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        //onView(withId(R.id.toggle_group_language)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateFromHomeToProfile() {
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.toggle_group_language)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateFromHomeToAddTest() {
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.button_add_test_report_to_firebase)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateFromHomeToVaccinationInformation() {
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.layout_not_vaccinated)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateFromHomeToQRCode() {
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.QRScreenQRCodeImg)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateFromHomeToAdvancedFeatures() {
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(0))
       // onView(withId(R.id.toggle_group_language)).check(matches(isDisplayed()))
    }
}

