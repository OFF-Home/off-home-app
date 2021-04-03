package com.offhome.app.ui.otherprofile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.offhome.app.R

class AboutThemFragment : Fragment() {

    companion object {
        fun newInstance() = AboutThemFragment()
    }

    private lateinit var viewModel: AboutThemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_about_me_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutThemViewModel::class.java)
        // TODO: Use the ViewModel
    }

}