package com.offhome.app.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.UserInfo
import com.google.gson.GsonBuilder
import com.offhome.app.MainActivity
import com.offhome.app.R
import com.offhome.app.model.profile.TopProfileInfo
import com.offhome.app.ui.otherprofile.OtherProfileActivity

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
    lateinit var estrelles: RatingBar
    // lateinit var aboutMeFragment :View
    private lateinit var editUsernameButton: ImageView
    private lateinit var layout1: AppBarLayout

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
        layout1 = view.findViewById(R.id.appBarLayout)

        val sectionsPagerAdapter = SectionsPagerAdapter(inflater.context, childFragmentManager)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        fragmentViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
        fragmentViewModel.getProfileInfo(context)
        fragmentViewModel.profileInfo.observe(
            viewLifecycleOwner,
            Observer { // aquest observer no arriba a executar-se però el de AboutMeFragment sí. NO ENTENC PERQUÈ
                val profileInfoVM = it ?: return@Observer

                // debug
                // Toast.makeText(context,"arribo al fragmentViewModel.ProfileInfo.observe()",Toast.LENGTH_LONG).show()

                textViewUsername.text = profileInfoVM.username
                estrelles.rating = profileInfoVM.estrelles.toFloat()
                // imageViewProfilePic.setImageDrawable(/**/) // TODO la foto
            }
        )

        // Toast.makeText(context,"s'executa onCreate de ProfileFragment",Toast.LENGTH_LONG).show()

        addEditButtons()

        return view
    }

    fun getViewModel(): ProfileFragmentViewModel {
        return fragmentViewModel
    }

    private fun addEditButtons() {
        editUsernameButton = ImageView(activity)
        // editUsernameButton.id = R.id.editUsernameButton

        // codi repetit de ProfileAboutMeFragment
        // to resize the drawable, we create a local drawable here
        val dr: Drawable = resources.getDrawable(android.R.drawable.ic_menu_edit)
        val bitmap: Bitmap = (dr as BitmapDrawable).bitmap
        // we scale it
        val d: Drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 70, 70, true))
        // we set our new scaled drawable "d"
        editUsernameButton.setImageDrawable(d)

        layout1.addView(editUsernameButton)

        editUsernameButton.setOnClickListener {
            //aqui no anirà això. ho he posat per a testejar el canvi a OtherProfile, d'una altra HU.
            canviAOtherProfile()
        }
    }
    //aixo es completament per a testejar
    private fun canviAOtherProfile() {

        //stub
        val userInfo = com.offhome.app.model.profile.UserInfo(email="yesThisIsVictor@gmail.com", username = "victorfer", password = "1234", birthDate = "12-12-2012",
            description = "Lou Spence (1917–1950) was a fighter pilot and squadron commander in the Royal Australian Air Force during World War II and the Korean War. In 1941 he was posted to North Africa with No. 3 Squadron, which operated P-40 Tomahawks and Kittyhawks; he was credited with shooting down two German aircraft and earned the Distinguished Flying Cross (DFC). He commanded No. 452 Squadron in ",
            followers = 200, following = 90, darkmode = 0, notifications = 0, estrelles = 3, tags="a b c d e", language = "esp")

        val intentCanviAOtherProfile = Intent(context, OtherProfileActivity::class.java) // .apply {        }
        intentCanviAOtherProfile.putExtra("user_info", GsonBuilder().create().toJson(userInfo))
        startActivity(intentCanviAOtherProfile)
    }
}
