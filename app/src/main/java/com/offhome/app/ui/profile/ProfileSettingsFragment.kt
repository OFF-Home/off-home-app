package com.offhome.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.offhome.app.R

class ProfileSettingsFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_settings_fragment, container, false)

        val profileFragment: ProfileFragment = parentFragment as ProfileFragment

        return view
    }

}
