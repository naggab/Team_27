package com.swtug.anticovid.vaccineInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swtug.anticovid.BaseFragment
import com.swtug.anticovid.R

class VaccineFragment: BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_vaccine_info, null)
    }
}