package com.swtug.anticovid.startUp

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.swtug.anticovid.R
import com.swtug.anticovid.utils.TestUtils
import com.swtug.anticovid.view.login.LoginFragment
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginNavigationTest {
    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        TestUtils.clearSharedPreferences(InstrumentationRegistry.getInstrumentation().targetContext)

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val mainScenario =
            launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_AntiCovid)

        mainScenario.withFragment {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.loginFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    private fun getContext(): Context {
        return InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testLoginWithWrongCredentials() {

        onView(withId(R.id.editTextTextEmailAddress)).perform(replaceText("test@test.com"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(replaceText("123456789"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.buttonLogin)).perform(click())
        Thread.sleep(3000)

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_wrong_password)
        onView(withId(R.id.txtLoginError)).check(matches(withText(compareString)))
        TestCase.assertEquals(navController.currentDestination?.id, R.id.loginFragment)
    }

    @Test
    fun testLoginWithCorretCredentials() {

        onView(withId(R.id.editTextTextEmailAddress)).perform(replaceText("test@test.com"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(replaceText("testtest"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        Thread.sleep(1000)
        onView(withId(R.id.buttonLogin)).perform(click())
        Thread.sleep(3000)

        TestCase.assertEquals(navController.currentDestination?.id, R.id.mainFragment)
    }

    @Test
    fun testLoginWithoutInputs() {
        onView(withId(R.id.buttonLogin)).perform(click())

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_no_email)
        onView(withId(R.id.txtEmailError)).check(matches(withText(compareString)))
    }
}

