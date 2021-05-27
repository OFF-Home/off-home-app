package com.offhome.app.ui.profile



import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.profile.TagData
import com.offhome.app.ui.otherprofile.OtherProfileActivity
import java.util.*

/**
 * Class *ProfileAboutMeFragment*
 *
 * Fragment for the "about me" section (page) of the Profile screen.
 * This class is one of the Views in this screen's MVVM's
 *
 * @author Ferran
 * @property profileVM reference to the ViewModel object of the entire Profile.
 * @property constraintLayout reference to the layout's ConstraintLayout
 * @property textViewProfileDescription reference to description TextView
 * @property textViewBirthDate reference to birth date TextView
 * @property textViewFollowerCount reference to follower count TextView
 * @property textViewFollowingCount reference to following count TextView
 * @property chipGroupTags reference to the tags ChipGroup
 * @property editDescriptionButton button that will be added programatically, to edit/save the user's description
 * @property addTagButton button that will be added programatically, to add tags / save changes
 * @property editIconDrawable drawable of the "edit" icon (a pencil)
 * @property saveIconDrawable drawable of the "save" icon (a diskette)
 * @property saveIconDrawable drawable of the "add" icon (a plus sign)
 * @property editTextProfileDescription editText to edit the user's description
 */
class ProfileAboutMeFragment : Fragment() {

    /**
     * ?
     */
    companion object {
        fun newInstance() = ProfileAboutMeFragment()
    }

    private lateinit var profileVM: ProfileFragmentViewModel

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var textViewProfileDescription: TextView
    private lateinit var textViewBirthDate: TextView
    private lateinit var textViewFollowerCount: TextView
    private lateinit var textViewFollowingCount: TextView
    private lateinit var chipGroupTags: ChipGroup

    private lateinit var editDescriptionButton: ImageView
    private lateinit var addTagButton: ImageView
    private lateinit var editIconDrawable: Drawable
    private lateinit var saveIconDrawable: Drawable
    private lateinit var addIconDrawable: Drawable
    private lateinit var editTextProfileDescription: EditText
    private lateinit var viewAsOtherProfile: Button

    /**
     * Override the onCreateView method
     *
     * Does the fragment inflation
     * Initializes the attributes
     * Initializes the attributes that reference layout objects
     *
     * observes the VM's live data for the result of the call made by the ProfileFragment to obtain the user's info (including tags)
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

        textViewProfileDescription = view.findViewById(R.id.textViewProfileDescription)
        textViewBirthDate = view.findViewById(R.id.textViewBirthDate)
        textViewFollowerCount = view.findViewById(R.id.textViewFollowerCount)
        textViewFollowingCount = view.findViewById(R.id.textViewFollowingCount)
        chipGroupTags = view.findViewById(R.id.chipGroupTags)
        constraintLayout = view.findViewById(R.id.aboutMeConstraintLayout)
        viewAsOtherProfile = view.findViewById(R.id.viewAsOtherProfile)

        // obtenir les dades de perfil del repo de ProfileFragment, aprofitant l'accés que aquest ha fet a backend
        val profileFragment: ProfileFragment = parentFragment as ProfileFragment

        profileVM = profileFragment.getViewModel()
        profileVM.profileInfo.observe(
            viewLifecycleOwner,
            Observer {
                val profileInfoVM = it ?: return@Observer
                // Toast.makeText(context,"arribo al profileVM.profileInfo.observe(); a AboutMeFragment",Toast.LENGTH_LONG).show()
                textViewProfileDescription.text = profileInfoVM.description
                textViewBirthDate.text = profileInfoVM.birthDate
                textViewFollowerCount.text = profileInfoVM.followers.toString()
                textViewFollowingCount.text = profileInfoVM.following.toString()
            }
        )
        profileVM.tags.observe(
            viewLifecycleOwner,
            Observer {
                val tagsVM = it ?: return@Observer
                omplirTagGroup(tagsVM)
                Log.d("tags", "tags arriben al fragment")
            }
        )

        viewAsOtherProfile.setOnClickListener {
            canviAOtherProfile()
        }

        // testing
        // omplirTagGroupStub()

        iniEditElements()
        iniEditionResultListeners() // TODO sobra?

        return view
    }

    /**
     * Initializes the listeners that observe the calls to backend made to edit parameters (description and tags)
     */
    private fun iniEditionResultListeners() {
        Log.d("iniEditionResultListe", "arribo al AboutMe::iniEditionResultListeners")
        iniDescriptionSetListener()
        iniTagDeletionListener()
        iniTagAdditionListener()
    }

    /**
     * Initializes the listener that observes the call to backend made to edit the description
     *
     * the listener removes itself after one use
     */
    private fun iniDescriptionSetListener() {
        profileVM.descriptionSetSuccessfully.observe(
            viewLifecycleOwner,
            Observer {
                val resultVM = it ?: return@Observer
                Log.d("setDescription", "salta el observer del fragment")
                if (resultVM.string() == "User has been updated") {
                    Toast.makeText(activity, R.string.description_updated_toast, Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(activity, R.string.description_update_error_toast, Toast.LENGTH_LONG).show()
                }
                // esborrem l'observer. Així, podem settejar-lo cada cop sense que s'acumulin
                profileVM.descriptionSetSuccessfully.removeObservers(viewLifecycleOwner) // hi ha una forma de treure només aquest observer, tipo removeObserver(this) pero nose com va
            }
        )
    }

    /**
     * Initializes the listener that observes the call to backend made to delete a tag
     *
     * the listener removes itself after one use
     */
    private fun iniTagDeletionListener() {
        profileVM.tagDeletedSuccessfully.observe(
            viewLifecycleOwner,
            Observer {
                val resultVM = it ?: return@Observer
                if (resultVM.string() == "Delete tag al usuario") {
                    Toast.makeText(activity, R.string.tag_deleted_toast, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, R.string.couldnt_delete_tag_toast, Toast.LENGTH_LONG).show()
                }
                profileVM.tagDeletedSuccessfully.removeObservers(viewLifecycleOwner)
            }
        )
    }

    /**
     * Initializes the listener that observes the call to backend made to add a tag
     *
     * the listener removes itself after one use
     */
    private fun iniTagAdditionListener() {
        profileVM.tagAddedSuccessfully.observe(
            viewLifecycleOwner,
            Observer {
                val resultVM = it ?: return@Observer
                if (resultVM.string() == "Insert tag al usuario") {
                    Toast.makeText(activity, R.string.tag_added_toast, Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(activity, R.string.couldnt_add_tag_toast, Toast.LENGTH_LONG).show()
                }
                profileVM.tagAddedSuccessfully.removeObservers(viewLifecycleOwner)
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
    }

    private fun omplirTagGroupStub() {
        var i: Int = 0
        val nTags: Int = 5
        while (i <nTags) {
            addTagToChipGroup("stub")
            ++i
        }
    }

    /**
     * creates and initializes a chip (visually, on the app. Nothing to do with backend) withe the parameter string
     *
     * Initializing includes setting the listener to the tag's "X" close button to delete it.
     *
     * @param tag tag to initialize
     */
    private fun addTagToChipGroup(tag: String) {
        val chip = layoutInflater.inflate(R.layout.deletable_chip_layout, chipGroupTags, false) as Chip
        chip.text = tag
        chipGroupTags.addView(chip)
        chip.setOnCloseIconClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
                .setMessage("Delete tag \"$tag\"?")
                .setPositiveButton("Delete") { dialog, which ->
                    profileVM.tagDeletedByUser(chip.text as String)
                    iniTagDeletionListener()
                    chipGroupTags.removeView(chip)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }

            val alertDialog = alertDialogBuilder.show()
        }
    }

    /**
     * Initializes the edition elements:
     *
     * Initializes the editDescriptionButton. It also sets its drawable and listener
     * Initializes the editTextUsername
     * Initializes the addTagButton and sets its listener
     */
    private fun iniEditElements() {
        iniEditDescriptionButton()
        // we set our new scaled drawable "d"
        editDescriptionButton.setImageDrawable(editIconDrawable)
        editDescriptionButton.setOnClickListener {
            changeDescriptionToEdit()
        }
        iniEditTextDescription()

        iniAddTagButton()
        addTagButton.setOnClickListener {
            addTagPressed()
        }
    }

    /**
     * Initializes the editDescriptionButton
     *
     * creates the object, sets id, initializes both drawables, inserts the View with its constraints in the constraint layout
     */
    private fun iniEditDescriptionButton() {
        editDescriptionButton = ImageView(activity)
        editDescriptionButton.id = R.id.editDescriptionButton // funciona somehow

        // we prepare the editIconDrawable, resizing it
        // to resize the drawable, we create a local drawable here
        val dr: Drawable = resources.getDrawable(android.R.drawable.ic_menu_edit)
        val bitmap: Bitmap = (dr as BitmapDrawable).bitmap
        // we scale it
        editIconDrawable = BitmapDrawable(
            resources,
            Bitmap.createScaledBitmap(bitmap, 70, 70, true)
        )

        // we prepare the saveIconDrawable, resizing it
        val dr2: Drawable = resources.getDrawable(android.R.drawable.ic_menu_save)
        val bitmap2: Bitmap = (dr2 as BitmapDrawable).bitmap
        // we scale it
        saveIconDrawable = BitmapDrawable(
            resources,
            Bitmap.createScaledBitmap(
                bitmap2,
                70,
                70,
                true
            )
        )

        // add the icon's constraints to the layout
        constraintLayout.addView(editDescriptionButton)
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout)
        constraintSet1.connect(
            R.id.editDescriptionButton,
            ConstraintSet.LEFT,
            R.id.textViewProfileDescriptionTitle,
            ConstraintSet.RIGHT,
            8
        )
        constraintSet1.connect(
            R.id.editDescriptionButton,
            ConstraintSet.TOP,
            R.id.textViewProfileDescriptionTitle,
            ConstraintSet.TOP,
            8
        )
        constraintSet1.applyTo(constraintLayout)
    }

    /**
     * Initializes the addTagButton
     *
     * creates the object, sets id, initializes the drawable and sets it, inserts the View with its constraints in the constraint layout
     */
    private fun iniAddTagButton() {
        addTagButton = ImageView(activity)
        addTagButton.id = R.id.addTagButton

        val dr: Drawable = resources.getDrawable(android.R.drawable.ic_menu_add)
        val bitmap: Bitmap = (dr as BitmapDrawable).bitmap
        // we scale it
        addIconDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 70, 70, true))

        addTagButton.setImageDrawable(addIconDrawable)

        constraintLayout.addView(addTagButton)
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout)
        constraintSet1.connect(
            R.id.addTagButton,
            ConstraintSet.LEFT,
            R.id.textViewTagsTitle,
            ConstraintSet.RIGHT,
            8
        )
        constraintSet1.connect(
            R.id.addTagButton,
            ConstraintSet.TOP,
            R.id.textViewTagsTitle,
            ConstraintSet.TOP,
            8
        )
        constraintSet1.applyTo(constraintLayout)
    }

    /**
     * Initializes the description EditText
     *
     * creates the object, sets id, inserts the View with its constraints and size i the constraint layout
     * initializes its visibility to gone
     */
    private fun iniEditTextDescription() {
        editTextProfileDescription = EditText(activity)
        editTextProfileDescription.id = R.id.editTextProfileDescription

        constraintLayout.addView(editTextProfileDescription)
        // add the EditText's constraints to the layout
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout)
        constraintSet1.connect(
            R.id.editTextProfileDescription,
            ConstraintSet.LEFT,
            R.id.aboutMeConstraintLayout,
            ConstraintSet.LEFT,
            16
        )
        constraintSet1.connect(
            R.id.editTextProfileDescription,
            ConstraintSet.RIGHT,
            R.id.aboutMeConstraintLayout,
            ConstraintSet.RIGHT,
            16
        )
        constraintSet1.connect(
            R.id.editTextProfileDescription,
            ConstraintSet.TOP,
            R.id.textViewProfileDescriptionTitle,
            ConstraintSet.BOTTOM,
            8
        )

        constraintSet1.clear(R.id.textViewProfileDescription, ConstraintSet.TOP)
        constraintSet1.connect(R.id.textViewProfileDescription, ConstraintSet.TOP, R.id.editTextProfileDescription, ConstraintSet.BOTTOM, 8) // a ver

        constraintSet1.applyTo(constraintLayout)

        val editTextlayoutParams: ViewGroup.LayoutParams = editTextProfileDescription.layoutParams
        editTextlayoutParams.width = 0

        editTextProfileDescription.visibility = View.GONE

        // set the max chars
        val filterArray: Array<InputFilter> = arrayOf(InputFilter.LengthFilter(280))
        editTextProfileDescription.filters = filterArray
    }

    /**
     * Changes the "state" of the description to editing
     *
     * meaning it changes the drawable to the "save" one, changes the listener, and changes the description textView for the EditText in the layout
     */
    private fun changeDescriptionToEdit() {
        editDescriptionButton.setImageDrawable(saveIconDrawable)
        textViewProfileDescription
        editDescriptionButton.setOnClickListener {
            val newDescription = editTextProfileDescription.text
            textViewProfileDescription.text = newDescription
            profileVM.descriptionChangedByUser(newDescription)
            iniDescriptionSetListener()
            changeDescriptionToDisplay()
        }
        editTextProfileDescription.setText(textViewProfileDescription.text)
        editTextProfileDescription.setHint(R.string.description)

        editTextProfileDescription.visibility = View.VISIBLE
        textViewProfileDescription.visibility = View.GONE
    }

    /**
     * Changes the "state" of the description to display
     *
     * meaning it changes the drawable to the "edit" one, changes the listener, and changes the description EditText for the textView in the layout
     */
    private fun changeDescriptionToDisplay() {
        editDescriptionButton.setImageDrawable(editIconDrawable)
        editDescriptionButton.setOnClickListener {
            changeDescriptionToEdit()
        }
        textViewProfileDescription.visibility = View.VISIBLE
        editTextProfileDescription.visibility = View.GONE
    }

    /**
     * Creates the dialogue to add a new tag.
     *
     * It checks whether the introduced text is empty. If it isn't, it adds the tag
     */
    private fun addTagPressed() {
        val textInputLayout = LayoutInflater.from(context).inflate(R.layout.text_input_for_dialogs, view as ViewGroup, false)
        val textInput = textInputLayout.findViewById<EditText>(R.id.editTextForInputDialogues)
        textInput.setHint(R.string.tag)

        val alertDialogBuilder = AlertDialog.Builder(context)
            .setTitle("Add a tag")
            .setView(textInputLayout)
            .setPositiveButton("Add") { dialog, which ->
                val tag = textInput.text.toString()
                if (tag.isEmpty()) {
                    Toast.makeText(context, R.string.tags_cant_be_empty_toast, Toast.LENGTH_LONG).show()
                    addTagPressed()
                } else
                    addTag(tag)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.show()
    }

    // afegeix tag a la app (chip) i al backend
    /**
     * Adds the tag in the app (meaning the chip) and starts the process to add it to the backend database
     * @param tag tag to add
     */
    private fun addTag(tag: String) {
        addTagToChipGroup(tag)
        profileVM.tagAddedByUser(tag)
        iniTagAdditionListener()
    }

    // aixo es completament per a testejar
    private fun canviAOtherProfile() {

        // stub
        val userInfo = com.offhome.app.model.profile.UserInfo(
            email = "yesThisIsVictor@gmail.com", username = "victorfer", uid = "102", birthDate = "12-12-2012",
            description = "Lou Spence (1917–1950) was a fighter pilot and squadron commander in the Royal Australian Air Force during World War II and the Korean War. In 1941 he was posted to North Africa with No. 3 Squadron, which operated P-40 Tomahawks and Kittyhawks; he was credited with shooting down two German aircraft and earned the Distinguished Flying Cross (DFC). He commanded No. 452 Squadron in ",
            followers = 200, following = 90, darkmode = 0, notifications = 0, estrelles = 3, tags = "a b c d e", language = "esp"
        )

        val intentCanviAOtherProfile = Intent(context, OtherProfileActivity::class.java) // .apply {        }
        intentCanviAOtherProfile.putExtra("user_info", GsonBuilder().create().toJson(userInfo))
        startActivity(intentCanviAOtherProfile)
    }
}
