package com.offhome.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R

class ProfileAboutBeFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileAboutBeFragment()
    }

    private lateinit var viewModel: ProfileAboutBeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_about_be_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileAboutBeViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
