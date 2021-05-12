package com.swtug.anticovid.vaccinationInfo

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.swtug.anticovid.R
import com.swtug.anticovid.TestUtils
import com.swtug.anticovid.models.Vaccination
import com.swtug.anticovid.repositories.PreferencesRepo
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class NotVaccinatedTest {
    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        TestUtils.clearSharedPreferences(InstrumentationRegistry.getInstrumentation().targetContext)

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val mainScenario =
            launchFragmentInContainer<NotVaccinatedFragment>(themeResId = R.style.Theme_AntiCovid)

        mainScenario.withFragment {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.notVaccinatedFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun testAddVaccineWithID() {
        onView(withId(R.id.textInput_vaccine_id)).perform(replaceText("test123"))

        val vaccination = Vaccination("test", Date(), Date(), "test")

        PreferencesRepo.saveVaccination(
            InstrumentationRegistry.getInstrumentation().targetContext,
            vaccination
        )

        onView(withId(R.id.button_add_vaccine)).perform(click())
        TestCase.assertEquals(navController.currentDestination?.id, R.id.vaccinatedFragment)
    }
}

