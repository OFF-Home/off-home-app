package com.offhome.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R

class ProfileFragment : Fragment() {

    /*companion object {
        fun newInstance() = ProfileFragment()
    }*/

    private lateinit var viewModel: ProfileViewModel
    lateinit var imageViewProfilePic:ImageView// = findViewById<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // treure?
        // viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        /*imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic)
        imageViewProfilePic.setImageDrawable( "@drawable/profile_pic_placeholder")*/

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java) // descomentar?
        // TODO: Use the ViewModel

    }
}
