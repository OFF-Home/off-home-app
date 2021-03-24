package com.offhome.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R

class ProfileFragment : Fragment() {

    /*companion object {
        fun newInstance() = ProfileFragment()
    }*/

    private lateinit var viewModel: ProfileViewModel
    lateinit var imageViewProfilePic: ImageView // = findViewById<ImageView>
    lateinit var buttonAboutMe: Button
    lateinit var fragmentDinsProfile: Fragment
    lateinit var aboutMeFragment: ProfileAboutBeFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // treure?
        // viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic)
        buttonAboutMe = view.findViewById(R.id.buttonAboutMe)
        fragmentDinsProfile = view.findViewById(R.id.fragmentDinsProfile) // aixo peta pq fragmentDinsProfile es com un link directe al fragment (crec), i considera q és un constraintLayout enlloc d'un fragment.

        // imageViewProfilePic.setImageDrawable( "@drawable/profile_pic_placeholder")
        buttonAboutMe.setOnClickListener {
            aboutMeFragment = ProfileAboutBeFragment() // inicialitzo
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java) // descomentar?
        // TODO: Use the ViewModel
    }

    private fun canviAAboutMeFragment() {
        val fragmentManager: FragmentManager = childFragmentManager

        fragmentManager.commit {
            add(R.id.fragmentDinsProfile, aboutMeFragment)

            // tambe afegir al back stack
        }

        // fragmentDinsProfile.childFragmentManager
    }
}
