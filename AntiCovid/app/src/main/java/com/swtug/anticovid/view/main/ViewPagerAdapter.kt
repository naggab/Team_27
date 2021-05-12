package com.swtug.anticovid.view.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swtug.anticovid.repositories.PreferencesRepo
import com.swtug.anticovid.view.vaccineInfo.NotVaccinatedFragment
import com.swtug.anticovid.view.vaccineInfo.VaccinatedFragment

class ViewStateAdapter(private val fragments: List<Fragment>, private val context: Context, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        if(position == itemCount - 1) {
            val vaccination = PreferencesRepo.getVaccination(context)
            return if(vaccination == null) {
                NotVaccinatedFragment()
            } else {
                VaccinatedFragment()
            }
        }
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size + 1
    }
}