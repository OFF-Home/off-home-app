package com.offhome.app.ui.otherprofile

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.offhome.app.R
import com.offhome.app.model.profile.UserInfo

class AboutThemFragment : Fragment() {

    companion object {
        fun newInstance() = AboutThemFragment()
    }

    //private lateinit var viewModel: AboutThemViewModel
    private lateinit var viewModel: OtherProfileViewModel

    private lateinit var textViewProfileDescription: TextView
    private lateinit var textViewBirthDate: TextView
    private lateinit var textViewFollowerCount: TextView
    private lateinit var textViewFollowingCount: TextView
    private lateinit var chipGroupTags : ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_about_me_fragment, container, false)

        //treure els log
        Log.d("inflate", "inflated successfully")
        if (parentFragment == null)
            Log.d("nullParent", "parentfragment is null")
        else
            Log.d("parent ok", "parentfragment is NOT null")

        //viewModel = ViewModelProvider(this).get(AboutThemViewModel::class.java)
       //viewModel = ViewModelProvider(parentFragment as ViewModelStoreOwner).get(OtherProfileViewModel::class.java)        //cast?
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(OtherProfileViewModel::class.java)

        textViewProfileDescription = view.findViewById(R.id.textViewProfileDescription)
        textViewBirthDate = view.findViewById(R.id.textViewBirthDate)
        textViewFollowerCount = view.findViewById(R.id.textViewFollowerCount)
        textViewFollowingCount = view.findViewById(R.id.textViewFollowingCount)
        chipGroupTags = view.findViewById(R.id.chipGroupTags)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val uinfo: UserInfo = viewModel.getUserInfo()

        textViewProfileDescription.text = uinfo.description
        textViewBirthDate.text = uinfo.birthDate
        textViewFollowerCount.text = uinfo.followers.toString()
        textViewFollowingCount.text = uinfo.following.toString()
        //metode tipo ProfileAboutMeFragment::omplirTagGroup()
    }
}