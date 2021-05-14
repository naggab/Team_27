package com.swtug.anticovid.view.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.swtug.anticovid.MainActivity
import com.swtug.anticovid.R
import com.swtug.anticovid.repositories.PreferencesRepo
import com.swtug.anticovid.view.addTestReport.AddTestReportFragment
import com.swtug.anticovid.view.profile.ProfileFragment
import com.swtug.anticovid.view.advancedFeatures.AdvancedFeatureFragment
import com.swtug.anticovid.view.qrCode.QRCodeFragment
import com.swtug.anticovid.view.testResults.TestResultFragment
import com.swtug.anticovid.view.vaccineInfo.VaccinationFragment


class MainFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        return layoutInflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        (requireActivity() as? MainActivity)?.setSupportActionBar(toolbar)

        initFields(view)
        setupLayout()
    }

    private fun setupLayout() {
        val fragments = listOf(
            AdvancedFeatureFragment() to getString(R.string.advanced_features),
            QRCodeFragment() to getString(R.string.valid_qr_code),
            VaccinationFragment() to getString(R.string.my_vaccine_information),
            AddTestReportFragment() to getString(R.string.add_test_report),
            ProfileFragment() to getString(R.string.profile),
            TestResultFragment() to getString(R.string.my_previous_test_reports)
        )

        val adapter = ViewPagerAdapter(fragments, childFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = (viewPager2.adapter as ViewPagerAdapter).getItemNameAtPosition(position)
            viewPager2.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun initFields(view: View) {
        viewPager2 = view.findViewById(R.id.pager)
        tabLayout = view.findViewById(R.id.tabLayout)
    }
}