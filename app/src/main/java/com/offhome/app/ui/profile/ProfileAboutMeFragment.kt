package com.offhome.app.ui.profile

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.offhome.app.R
import com.offhome.app.model.profile.ProfileRepository

class ProfileAboutMeFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileAboutMeFragment()
    }

    private lateinit var viewModel: ProfileAboutMeViewModel
    private lateinit var textViewProfileDescription: TextView
    private lateinit var textViewBirthDate: TextView
    private lateinit var textViewFollowerCount: TextView
    private lateinit var textViewFollowingCount: TextView
    private lateinit var chipGroupTags : ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_about_me_fragment, container, false)

        viewModel = ViewModelProvider(this).get(ProfileAboutMeViewModel::class.java)

        textViewProfileDescription = view.findViewById(R.id.textViewProfileDescription)
        textViewBirthDate = view.findViewById(R.id.textViewBirthDate)
        textViewFollowerCount = view.findViewById(R.id.textViewFollowerCount2)
        textViewFollowingCount = view.findViewById(R.id.textViewFollowingCount2)
        chipGroupTags = view.findViewById(R.id.chipGroupTags)


        /*viewModel.ProfileInfo.observe(
            viewLifecycleOwner,
            Observer {
                val profileInfoVM = it ?: return@Observer

                textViewProfileDescription.text = profileInfoVM.description
                textViewBirthDate.text = profileInfoVM.birthDate
                textViewFollowerCount.text = profileInfoVM.followers.toString()
                textViewFollowingCount.text = profileInfoVM.following.toString()
            }
        )*/
        //viewModel.getProfileInfo()

        //obtenir les dades de perfil del repo de ProfileFragment, aprofitant l'accés que aquest ha fet a backend
        val profileFragment:ProfileFragment = parentFragment as ProfileFragment
        val profileRepo:ProfileRepository = profileFragment.getViewModel().getRepository()

        profileRepo.userInfo?.observe(
            viewLifecycleOwner,
            Observer {
                val profileInfoVM = it ?: return@Observer
                textViewProfileDescription.text = profileInfoVM.description
                textViewBirthDate.text = profileInfoVM.birthDate
                textViewFollowerCount.text = profileInfoVM.followers.toString()
                textViewFollowingCount.text = profileInfoVM.following.toString()

                omplirTagGroup(profileInfoVM.tags)
            }
        )
        //val profileInfoRepo = profileRepo.getProfileInfo("victorfer")

        //testing
        omplirTagGroup("aixo no s'arriba a llegir, lol")

        Toast.makeText(context, "arribo a omplirTagGroup()", Toast.LENGTH_LONG).show()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun omplirTagGroup(tagString:String) {
        var i = 0
        val nTags = 10 //stub
        while (i<nTags) {
            //agafar tag del string
            val tag1 = Chip(context); tag1.text = "stub"; chipGroupTags.addView(tag1)
            tag1.chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.primary_light))      // Color("@color/primary_light")    ;  // R.id.@color/primary_light
            tag1.chipStrokeWidth = 5F
            ++i
        }
    }

    private fun omplirTagGroupPlaceholder() {
        val tag1 = Chip(context); tag1.text = "tag1"; chipGroupTags.addView(tag1)
        val tag2 = Chip(context); tag2.text = "tag2"; chipGroupTags.addView(tag2)
        val tag3 = Chip(context); tag3.text = "tag3"; chipGroupTags.addView(tag3)
    }
}
