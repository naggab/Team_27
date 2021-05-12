package com.swtug.anticovid.view.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.swtug.anticovid.MainActivity
import com.swtug.anticovid.R
import com.swtug.anticovid.repositories.PreferencesRepo
import com.swtug.anticovid.view.addTestReport.AddTestReportFragment
import com.swtug.anticovid.view.profile.ProfileFragment
import com.swtug.anticovid.view.qrCode.AdvancedFeatureFragment
import com.swtug.anticovid.view.qrCode.QRCodeFragment
import com.swtug.anticovid.view.testResults.TestResultFragment
import com.swtug.anticovid.view.vaccineInfo.VaccinationFragment


class MainFragment : Fragment() {
    private lateinit var navController: NavController

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                PreferencesRepo.deleteUser(requireContext())
                PreferencesRepo.deleteVaccination(requireContext())
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        initFields(view)
        initListeners()
        setupLayout()
    }

    private fun setupLayout() {
        val fragments = listOf(
            AdvancedFeatureFragment(),
            QRCodeFragment(),
            VaccinationFragment(),
            AddTestReportFragment(),
            ProfileFragment(),
            TestResultFragment(),
        )

        val adapter = ViewStateAdapter(fragments, requireContext(), childFragmentManager, lifecycle)
        viewPager2.adapter = adapter
    }

    private fun initListeners() {
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    private fun initFields(view: View) {
        viewPager2 = view.findViewById(R.id.pager)
        tabLayout = view.findViewById(R.id.tabLayout)
    }
}