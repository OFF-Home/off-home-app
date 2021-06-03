package com.offhome.app.ui.otherprofile



import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.TagData
import com.offhome.app.data.model.UserInfo

/**
 * Fragment for the "about them" part of the OtherProfile screen
 * This class is one of the Views in this screen's MVVM's
 *
 * @property viewModel reference to the ViewModel object of the entire OtherProfile.
 * @property textViewProfileDescription reference to description TextView
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
    private lateinit var textViewFollowerCount: TextView
    private lateinit var textViewFollowingCount: TextView
    private lateinit var chipGroupTags: ChipGroup
    private lateinit var gridLayout: GridLayout

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
        textViewFollowerCount = view.findViewById(R.id.textViewFollowerCount)
        textViewFollowingCount = view.findViewById(R.id.textViewFollowingCount)
        chipGroupTags = view.findViewById(R.id.chipGroupTags)

        gridLayout = view.findViewById(R.id.gridLayout)

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
        textViewFollowerCount.text = uinfo.followers.toString()
        textViewFollowingCount.text = uinfo.following.toString()

        viewModel.getUserTags().observe(
            viewLifecycleOwner,
            Observer {

                if (it is Result.Success) {
                    omplirTagGroup(it.data)
                } else {
                    Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
                }
            }
        )
        getAchievements()
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
            Log.d("tags otherprofile", "tags empty")
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

    private fun getAchievements() {
        viewModel.getAchievements().observe(
            viewLifecycleOwner,
            Observer {
                if (it is Result.Success) {
                    for ((index, x) in it.data.withIndex()) {
                        val imageView = ImageView(requireContext())
                        var drawable2: Drawable?
                        if (x.nom.contains("DIAMOND", true)) {
                            drawable2 = ResourcesCompat.getDrawable(
                                requireContext().resources,
                                R.drawable.trophy_diamond_small,
                                requireContext().theme
                            )
                        } else if (x.nom.contains("PLATINUM", true)) {
                            drawable2 = ResourcesCompat.getDrawable(
                                requireContext().resources,
                                R.drawable.trophy_platinum_small,
                                requireContext().theme
                            )
                        } else if (x.nom.contains("BRONZE", true)) {
                            drawable2 = ResourcesCompat.getDrawable(
                                requireContext().resources,
                                R.drawable.trophy_bronze_small,
                                requireContext().theme
                            )
                        } else if (x.nom.contains("SILVER", true)) {
                            drawable2 = ResourcesCompat.getDrawable(
                                requireContext().resources,
                                R.drawable.trophy_silver_small,
                                requireContext().theme
                            )
                        } else {
                            drawable2 = ResourcesCompat.getDrawable(
                                requireContext().resources,
                                R.drawable.trophy_gold_small,
                                requireContext().theme
                            )
                        }
                        imageView.setImageDrawable(drawable2)
                        gridLayout.addView(imageView)
                        imageView.setOnClickListener {
                            Toast.makeText(context, x.descripcio, Toast.LENGTH_SHORT).show()
                        }
                    }

                } else if (it is Result.Error) {
                    Log.d("GET", it.exception.message.toString())
                }
            })
    }
}
