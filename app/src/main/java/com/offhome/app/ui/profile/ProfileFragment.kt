package com.offhome.app.ui.profile

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.offhome.app.R
import com.offhome.app.model.profile.ProfileRepository

/**
 * Class *ProfileFragment*
 *
 * Fragment for the Profile screen. On its ViewPager it can show either of the 3 fragments: MyActivities, AboutMe, Settings.
 * This class is one of the Views in this screen's MVVM's
 *
 * @author Pau and Ferran
 * @property fragmentViewModel reference to the ViewModel object
 * @property imageViewProfilePic reference to profile pic ImageView
 * @property textViewUsername reference to the username TextView
 */
class ProfileFragment : Fragment() {
    private lateinit var fragmentViewModel: ProfileFragmentViewModel
    lateinit var imageViewProfilePic: ImageView
    lateinit var textViewUsername: TextView
    lateinit var estrelles : RatingBar
    //lateinit var aboutMeFragment :View

    /**
     * Override the onCreateView method
     *
     * Does the fragment inflation
     * Initializes the attributes
     * Sets up the ViewPager and its tabs
     *
     * Makes the call to the VM to obtain the topProfileInfo and observes its live data for the result
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return returns the view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic)
        textViewUsername = view.findViewById(R.id.textViewUsername)
        estrelles = view.findViewById(R.id.ratingBarEstrellesUsuari)

        val sectionsPagerAdapter = SectionsPagerAdapter(inflater.context, childFragmentManager)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        fragmentViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
        fragmentViewModel.profileInfo.observe(
            viewLifecycleOwner,
            Observer {          //aquest observer no arriba a executar-se però el de AboutMeFragment sí. NO ENTENC PERQUÈ
                val profileInfoVM = it ?: return@Observer

                //debug
                //Toast.makeText(context,"arribo al fragmentViewModel.ProfileInfo.observe()",Toast.LENGTH_LONG).show()

                textViewUsername.text = profileInfoVM.username
                estrelles.numStars = profileInfoVM.estrelles
                // imageViewProfilePic.setImageDrawable(/**/) // TODO la foto
            }
        )

        Toast.makeText(context,"s'executa onCreate de ProfileFragment",Toast.LENGTH_LONG).show()

        fragmentViewModel.getProfileInfo(context)

        return view
    }

    fun getViewModel():ProfileFragmentViewModel {
        return fragmentViewModel
    }
}
