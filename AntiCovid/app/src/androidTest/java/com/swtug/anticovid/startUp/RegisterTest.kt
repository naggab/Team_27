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
import com.swtug.anticovid.view.register.RegisterFragment
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegisterTest {
    private lateinit var navController: TestNavHostController
    private val TEST_USER_MAIL: String = "test@test.com"
    private val TEST_USER_PWD: String = "testtest"
    private val TEST_USER_SECID: String = "TestingAcc"

    @Before
    fun setup() {
        TestUtils.clearSharedPreferences(getContext())

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val mainScenario =
            launchFragmentInContainer<RegisterFragment>(themeResId = R.style.Theme_AntiCovid)

        mainScenario.withFragment {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.registerFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    private fun fillOutRegisterForm(email: String, pwd: String, secID: String) {
        onView(withId(R.id.editTextEmail)).perform(replaceText(email))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(replaceText(pwd))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextAddress)).perform(replaceText("Im a test dummy 2"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(replaceText("Test"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextSureName)).perform(replaceText("Dont delete me"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextPhoneNumber)).perform(replaceText("+3030303030303030"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.editTextSecurityID)).perform(replaceText(secID))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
    }

    private fun getContext(): Context {
        return InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testRegisterWithoutInputs() {
        onView(withId(R.id.buttonSignUp)).perform(click())

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_empty_fields)
        onView(withId(R.id.editTextInputError)).check(matches(withText(compareString)))
    }

    @Test
    fun testRegisterWithoutPwd() {
        fillOutRegisterForm(TEST_USER_MAIL, "", TEST_USER_SECID)
        Thread.sleep(1000)

        onView(withId(R.id.buttonSignUp)).perform(click())

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_empty_fields)
        onView(withId(R.id.editTextInputError)).check(matches(withText(compareString)))
    }

    @Test
    fun testRegisterWithTooShortPwd() {
        fillOutRegisterForm(TEST_USER_MAIL, "testtes", TEST_USER_SECID)
        Thread.sleep(1000)

        onView(withId(R.id.buttonSignUp)).perform(click())

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_pwd_too_short)
        onView(withId(R.id.editTextInputError)).check(matches(withText(compareString)))
    }

    @Test
    fun testRegisterWithTooShortSecID() {
        fillOutRegisterForm(TEST_USER_MAIL, TEST_USER_PWD, "123456789")
        Thread.sleep(1000)

        onView(withId(R.id.buttonSignUp)).perform(click())

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_secID_lenth)
        onView(withId(R.id.editTextInputError)).check(matches(withText(compareString)))
    }

    @Test
    fun testRegisterWithTooLongSecID() {
        fillOutRegisterForm(TEST_USER_MAIL, TEST_USER_PWD, "12345678910")
        Thread.sleep(1000)

        onView(withId(R.id.buttonSignUp)).perform(click())

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_secID_lenth)
        onView(withId(R.id.editTextInputError)).check(matches(withText(compareString)))
    }

    @Test
    fun testRegisterWithExistingUser() {
        fillOutRegisterForm(TEST_USER_MAIL, TEST_USER_PWD, TEST_USER_SECID)
        Thread.sleep(1000)

        onView(withId(R.id.buttonSignUp)).perform(click())
        Thread.sleep(3000)

        val compareString = TestUtils.getResoureceString(getContext(), R.string.error_user_exists)
        onView(withId(R.id.editTextInputError)).check(matches(withText(compareString)))
    }

    @Test
    fun testRegisterCancelNavigation() {
        onView(withId(R.id.buttonCancelSignUp)).perform(click())
        TestCase.assertEquals(navController.currentDestination?.id, R.id.loginFragment)
    }
}

