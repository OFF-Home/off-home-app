package com.offhome.app.ui.profile

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.offhome.app.R

/**
 * Class *ProfileFragment*
 *
 * Fragment for the Profile screen. On its ViewPager it can show either of the 3 fragments: MyActivities, AboutMe, Settings.
 * This class is one of the Views in this screen's MVVM's
 *
 * author Pau and Ferran
 * @property fragmentViewModel
 * @property imageViewProfilePic
 * @property textViewUsername
 */
class ProfileFragment : Fragment() {
    private lateinit var fragmentViewModel: ProfileFragmentViewModel
    lateinit var imageViewProfilePic: ImageView
    lateinit var textViewUsername: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic)
        textViewUsername = view.findViewById(R.id.textViewUsername)
        val sectionsPagerAdapter = SectionsPagerAdapter(inflater.context, childFragmentManager)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        fragmentViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
        fragmentViewModel.topProfileInfo.observe(
            viewLifecycleOwner,
            Observer {
                val topProfileInfoVM = it ?: return@Observer

                // TODO els altres
                textViewUsername.text = topProfileInfoVM.username
                // imageViewProfilePic.setImageDrawable(/**/)
            }
        )

        fragmentViewModel.getTopProfileInfo()

        return view
    }
}
