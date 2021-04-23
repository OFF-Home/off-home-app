package com.offhome.app.ui.profile

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.offhome.app.R
import com.offhome.app.model.profile.TagData
import java.util.*


class ProfileAboutMeFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileAboutMeFragment()
    }

    private lateinit var viewModel: ProfileAboutMeViewModel     //TODO té pinta que la classe ProfileAboutMeViewModel la borraré i faré servir ProfileFragmentViewModel
    private lateinit var profileVM: ProfileFragmentViewModel    //fem servir el viewModel de Profile
    private lateinit var textViewProfileDescription: TextView
    private lateinit var textViewBirthDate: TextView
    private lateinit var textViewFollowerCount: TextView
    private lateinit var textViewFollowingCount: TextView
    private lateinit var chipGroupTags: ChipGroup

    private lateinit var editDescriptionButton: ImageView
    private lateinit var addTagButton:ImageView
    private lateinit var constraintLayout2: ConstraintLayout

    private lateinit var editIconDrawable:Drawable
    private lateinit var saveIconDrawable:Drawable
    private lateinit var addIconDrawable:Drawable
    private lateinit var editTextProfileDescription : EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_about_me_fragment, container, false)

        viewModel = ViewModelProvider(this).get(ProfileAboutMeViewModel::class.java)

        textViewProfileDescription = view.findViewById(R.id.textViewProfileDescription)
        textViewBirthDate = view.findViewById(R.id.textViewBirthDate)
        textViewFollowerCount = view.findViewById(R.id.textViewFollowerCount)
        textViewFollowingCount = view.findViewById(R.id.textViewFollowingCount)
        chipGroupTags = view.findViewById(R.id.chipGroupTags)
        constraintLayout2 = view.findViewById(R.id.aboutMeConstraintLayout)

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
                Log.d("tags","tags arriben al fragment")
            }
        )

        // testing
        omplirTagGroupStub()

        iniEditElements()
        iniEditionResultListeners()

        return view
    }

    private fun iniEditionResultListeners() {
        Log.d("iniEditionResultListe", "arribo al AboutMe::iniEditionResultListeners")
        profileVM.setDescriptionSuccessfully.observe(   //igual que el de ProfileFragment (setUsernameSuccessfully.observe) no salta. quan arregli un, fer copia-pega a l'altre.
            viewLifecycleOwner,
            Observer {
                val resultVM = it ?: return@Observer
                if (resultVM) {
                    Toast.makeText(activity, R.string.description_updated_toast, Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(activity, R.string.description_update_error_toast, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun omplirTagGroup(tagList:List<TagData>) {

        for (tagData in tagList) {
            addTagToChipGroup(tagData.nomTag)
        }
    }

    private fun omplirTagGroupStub() {
        var i:Int = 0
        val nTags:Int = 5
        while (i <nTags) {
            addTagToChipGroup("stub")
            ++i
        }
    }

    //crea i inicialitza un chip (visual, a la app, res de backend) amb el string passat
    //inicialitzar inclou posar-li el listener a la "X" de esborrar que crea un dialeg de confirmació.
    private fun addTagToChipGroup(tag: String) {
        val chip = layoutInflater.inflate(R.layout.deletable_chip_layout, chipGroupTags, false) as Chip
        chip.text = tag
        chipGroupTags.addView(chip)
        chip.setOnCloseIconClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
                .setMessage("Delete tag \"$tag\"?")
                .setPositiveButton("Delete") { dialog, which ->
                    profileVM.tagDeletedByUser(chip.text as String)
                    chipGroupTags.removeView(chip)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }

            val alertDialog = alertDialogBuilder.show()
        }
    }

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

    // finds the view, initiates it with its constraints, initiates the 2 drawables
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
            resources, Bitmap.createScaledBitmap(
                bitmap2,
                70,
                70,
                true
            )
        )

        // add the icon's constraints to the layout
        constraintLayout2.addView(editDescriptionButton)
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout2)
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
        constraintSet1.applyTo(constraintLayout2)
    }

    private fun iniAddTagButton() {
        addTagButton = ImageView(activity)
        addTagButton.id = R.id.addTagButton

        val dr: Drawable = resources.getDrawable(android.R.drawable.ic_menu_add)
        val bitmap: Bitmap = (dr as BitmapDrawable).bitmap
        // we scale it
        addIconDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 70, 70, true))

        addTagButton.setImageDrawable(addIconDrawable)

        constraintLayout2.addView(addTagButton)
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout2)
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
        constraintSet1.applyTo(constraintLayout2)
    }

    private fun iniEditTextDescription() {
        editTextProfileDescription = EditText(activity)
        editTextProfileDescription.id = R.id.editTextProfileDescription

        constraintLayout2.addView(editTextProfileDescription)
        // add the EditText's constraints to the layout
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout2)
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

        constraintSet1.applyTo(constraintLayout2)

        val editTextlayoutParams: ViewGroup.LayoutParams = editTextProfileDescription.layoutParams
        editTextlayoutParams.width = 0

        editTextProfileDescription.visibility = View.GONE
    }

    private fun changeDescriptionToEdit() {
        editDescriptionButton.setImageDrawable(saveIconDrawable)
        textViewProfileDescription
        editDescriptionButton.setOnClickListener {
            textViewProfileDescription.text = editTextProfileDescription.text
            profileVM.descriptionChangedByUser(editTextProfileDescription.text)
            changeDescriptionToDisplay()
        }
        editTextProfileDescription.setText(textViewProfileDescription.text)
        editTextProfileDescription.setHint(R.string.description)

        editTextProfileDescription.visibility = View.VISIBLE
        textViewProfileDescription.visibility = View.GONE
    }

    private fun changeDescriptionToDisplay() {
        editDescriptionButton.setImageDrawable(editIconDrawable)
        editDescriptionButton.setOnClickListener {
            changeDescriptionToEdit()
        }
        textViewProfileDescription.visibility = View.VISIBLE
        editTextProfileDescription.visibility = View.GONE
    }

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
                    Toast.makeText(context, R.string.tags_cant_be_empty_toast,Toast.LENGTH_LONG).show()
                    addTagPressed()
                }
                else
                    addTag(tag)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.show()
    }

    //afegeix tag a la app (chip) i al backend
    private fun addTag(tag: String) {
        addTagToChipGroup(tag)
        val unixTime = (System.currentTimeMillis() % 1000000L).toString()
        //profileVM.tagAddedByUser(unixTime)// stub amb unix time stamp per si faig moltes insercions iguals a BD
        profileVM.tagAddedByUser(tag)
    }

}
