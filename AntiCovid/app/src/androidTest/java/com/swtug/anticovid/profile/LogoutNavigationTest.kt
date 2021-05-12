package com.swtug.anticovid.profile

import android.content.Context
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.dx.command.Main
import com.swtug.anticovid.R
import com.swtug.anticovid.view.main.MainFragment
import com.swtug.anticovid.view.profile.ProfileFragment
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class LogoutNavigationTest {
    private lateinit var navController: TestNavHostController
    private lateinit var mainScenario: FragmentScenario<MainFragment>

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        mainScenario = launchFragmentInContainer<MainFragment>(themeResId = R.style.Theme_AntiCovid)

        mainScenario.withFragment {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.mainFragment)
            Navigation.setViewNavController(requireView(), navController)
        }


    }


    @Test
    fun testLogout() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val addMenuItem = ActionMenuItem(context, 0, R.id.logout, 0, 0, null)

        //Call onOptionsItemSelected with the dummy menu item
        mainScenario.onFragment { fragment ->
            fragment.onOptionsItemSelected(addMenuItem)
        }
        TestCase.assertEquals(navController.currentDestination?.id, R.id.loginFragment)
    }
}

