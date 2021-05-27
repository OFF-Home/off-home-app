package com.offhome.app.ui.otherprofile



import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.offhome.app.R
import com.offhome.app.model.profile.TagData
import com.offhome.app.model.profile.UserInfo

/**
 * Fragment for the "about them" part of the OtherProfile screen
 * This class is one of the Views in this screen's MVVM's
 *
 * @property viewModel reference to the ViewModel object of the entire OtherProfile.
 * @property textViewProfileDescription reference to description TextView
 * @property textViewBirthDate reference to birth date TextView
 * @property textViewFollowerCount reference to follower count TextView
 * @property textViewFollowingCount reference to following count TextView
 * @property chipGroupTags reference to the tags ChipGroup
 *
 */
class AboutThemFragment : Fragment() {

    /**
     * ?
     */
    companion object {
        fun newInstance() = AboutThemFragment()
    }

    private lateinit var viewModel: OtherProfileViewModel

    private lateinit var textViewProfileDescription: TextView
    private lateinit var textViewBirthDate: TextView
    private lateinit var textViewFollowerCount: TextView
    private lateinit var textViewFollowingCount: TextView
    private lateinit var chipGroupTags: ChipGroup

    /**
     * Override the onCreateView method
     *
     * Does the fragment inflation
     * Initializes the attributes
     * Initializes the attributes that reference layout objects
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
        val view = inflater.inflate(R.layout.profile_about_me_fragment, container, false)

        viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(OtherProfileViewModel::class.java)

        textViewProfileDescription = view.findViewById(R.id.textViewProfileDescription)
        textViewBirthDate = view.findViewById(R.id.textViewBirthDate)
        textViewFollowerCount = view.findViewById(R.id.textViewFollowerCount)
        textViewFollowingCount = view.findViewById(R.id.textViewFollowingCount)
        chipGroupTags = view.findViewById(R.id.chipGroupTags)

        return view
    }

    /**
     * Override the onActivityCreated method
     *
     * gets the User Info and tags from the VM
     * @param savedInstanceState
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val uinfo: UserInfo = viewModel.getUserInfo()

        textViewProfileDescription.text = uinfo.description
        textViewBirthDate.text = uinfo.birthDate
        textViewFollowerCount.text = uinfo.followers.toString()
        textViewFollowingCount.text = uinfo.following.toString()

        viewModel.getUserTags()
        viewModel.userTagsFromBack.observe(
            viewLifecycleOwner,
            Observer {
                Log.d("tags", "tags arriben al aboutThemFragment1")
                val tagsVM = it ?: return@Observer
                //omplirTagGroup(tagsVM)
                Log.d("tags", "tags arriben al aboutThemFragment2")


                if (it.toString().contains("unsuccessful"))
                    Toast.makeText(context,"unsuccessful", Toast.LENGTH_LONG).show()
                else if (it.toString().contains("failure"))
                    Toast.makeText(context,"communication failure", Toast.LENGTH_LONG).show()
                else {
                    //TODO esto
                    omplirTagGroup(tagsVM.getDataOrNull() as List<TagData>)
                }
            }
        )
    }

    // old, stub.
    private fun omplirTagGroup() {
        val tag1 = Chip(context); tag1.text = "stub"; chipGroupTags.addView(tag1)
        tag1.chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.primary_light))
        tag1.chipStrokeWidth = 5F
    }

    /**
     * fills the tags ChipGroup with the tags passed
     *
     * @param tagList list of tags
     */
    private fun omplirTagGroup(tagList: List<TagData>) {
        for (tagData in tagList) {
            addTagToChipGroup(tagData.nomTag)
        }
        if (tagList.isEmpty()) {
            //TODO treure
            Toast.makeText(context, "tags empty", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * creates and initializes a chip (visually, on the app. Nothing to do with backend) withe the parameter string
     *
     * @param tag tag to initialize
     */
    private fun addTagToChipGroup(tag: String) {
        val chip = Chip(context)
        chip.text = tag
        chip.chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.primary_light))
        chip.chipStrokeWidth = 5F
        chipGroupTags.addView(chip)
    }

    /**
     * updates the follower count text view according to the data in the VM
     */
    fun updateFollowes() {
        textViewFollowerCount.text = viewModel.getUserInfo().followers.toString()
    }
}
