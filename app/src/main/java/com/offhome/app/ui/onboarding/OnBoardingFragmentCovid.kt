package com.offhome.app.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.offhome.app.R

class OnBoardingFragmentCovid : Fragment() {

    companion object {
        fun newInstance() = OnBoardingFragmentCovid()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding_covid, container, false)
    }

}
