package com.offhome.app.ui.profile

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

class ProfileAboutMeFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileAboutMeFragment()
    }

    private lateinit var viewModel: ProfileAboutMeViewModel     //té pinta que la classe ProfileAboutMeViewModel la borraré i faré servir ProfileFragmentViewModel
    private lateinit var profileVM: ProfileFragmentViewModel    //fem servir el viewModel de Profile
    private lateinit var textViewProfileDescription: TextView
    private lateinit var textViewBirthDate: TextView
    private lateinit var textViewFollowerCount: TextView
    private lateinit var textViewFollowingCount: TextView
    private lateinit var chipGroupTags: ChipGroup

    private lateinit var editDescriptionButton: ImageView
    private lateinit var constraintLayout2: ConstraintLayout

    private lateinit var editIconDrawable:Drawable
    private lateinit var saveIconDrawable:Drawable
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

                omplirTagGroup(profileInfoVM.tags)
            }
        )

        // testing
        omplirTagGroup("aquest string encara no el llegim")

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
                    Toast.makeText(activity,R.string.description_updated_toast, Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(activity,R.string.description_update_error_toast, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun omplirTagGroup(tagString: String) {
        var i = 0
        val nTags = 10 // stub
        while (i <nTags) {
            // aixo és una mena de placeholder. aqui acabarem fent una conversió de JSon o algo aixi

            // intents vells
            //val tag1 = Chip(context/*, null, android.style.Widget.MaterialComponents.Chip.Entry*/);
            //val tag1 = Chip(context, null, R.attr.chipStyle);
            //val tag2 = Chip(context, null, R.attr.customChipStyleDeletable);
            //val tag1 = Chip(context, null, R.style.Widget_MaterialComponents_Chip_Entry);
            //val tag1 = Chip(context, null, R.style.Widget_MaterialComponents_Chip_Action);
            //val tag2 = Chip(context, null, R.style.deletable_chip) ;
            val chip3 = layoutInflater.inflate(R.layout.deletable_chip_layout, chipGroupTags, false) as Chip
            //Log.d("chips", "chip tag1 created")
            chip3.text = "stub";
            chipGroupTags.addView(chip3)
            //tag2.chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.primary_light)) // Color("@color/primary_light")    ;  // R.id.@color/primary_light
            //chip3.chipStrokeWidth = 5F

            chip3.setOnCloseIconClickListener {
                Toast.makeText(context,"chip3.setOnCloseIconClickListener",Toast.LENGTH_LONG).show()
                //nice! aquest listener és el de la "X" de borrar.
            }

            ++i
        }
    }

    private fun omplirTagGroupPlaceholder() {
        val tag1 = Chip(context); tag1.text = "tag1"; chipGroupTags.addView(tag1)
        val tag2 = Chip(context); tag2.text = "tag2"; chipGroupTags.addView(tag2)
        val tag3 = Chip(context); tag3.text = "tag3"; chipGroupTags.addView(tag3)
    }

    private fun iniEditElements() {
        iniEditDescriptionButton()
        // we set our new scaled drawable "d"
        editDescriptionButton.setImageDrawable(editIconDrawable)
        editDescriptionButton.setOnClickListener{
            changeDescriptionToEdit()
        }
        iniEditTextDescription()
    }

    //finds the view, initiates it with its constraints, initiates the 2 drawables
    private fun iniEditDescriptionButton() {
        editDescriptionButton = ImageView(activity)
        editDescriptionButton.id = R.id.editDescriptionButton // funciona somehow

        //we prepare the editIconDrawable, resizing it
        // to resize the drawable, we create a local drawable here
        val dr: Drawable = resources.getDrawable(android.R.drawable.ic_menu_edit)
        val bitmap: Bitmap = (dr as BitmapDrawable).bitmap
        // we scale it
        editIconDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 70, 70, true))

        //we prepare the saveIconDrawable, resizing it
        val dr2: Drawable = resources.getDrawable(android.R.drawable.ic_menu_save)
        val bitmap2: Bitmap = (dr2 as BitmapDrawable).bitmap
        // we scale it
        saveIconDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap2, 70, 70, true))

        //add the icon's constraints to the layout
        constraintLayout2.addView(editDescriptionButton)
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout2)
        constraintSet1.connect(R.id.editDescriptionButton, ConstraintSet.LEFT, R.id.textViewProfileDescriptionTitle, ConstraintSet.RIGHT, 8)
        constraintSet1.applyTo(constraintLayout2)
    }

    private fun iniEditTextDescription() {
        editTextProfileDescription = EditText(activity)
        editTextProfileDescription.id = R.id.editTextProfileDescription

        constraintLayout2.addView(editTextProfileDescription)
        //add the EditText's constraints to the layout
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout2)
        constraintSet1.connect(R.id.editTextProfileDescription, ConstraintSet.LEFT, R.id.aboutMeConstraintLayout, ConstraintSet.LEFT, 16)
        constraintSet1.connect(R.id.editTextProfileDescription, ConstraintSet.RIGHT, R.id.aboutMeConstraintLayout, ConstraintSet.RIGHT, 16)
        constraintSet1.connect(R.id.editTextProfileDescription, ConstraintSet.TOP, R.id.textViewProfileDescriptionTitle, ConstraintSet.BOTTOM, 8)

        constraintSet1.clear(R.id.textViewProfileDescription,ConstraintSet.TOP)
        constraintSet1.connect(R.id.textViewProfileDescription, ConstraintSet.TOP, R.id.editTextProfileDescription, ConstraintSet.BOTTOM, 8)    //a ver

        constraintSet1.applyTo(constraintLayout2)

        val editTextlayoutParams: ViewGroup.LayoutParams = editTextProfileDescription.layoutParams
        editTextlayoutParams.width = 0

        editTextProfileDescription.visibility = View.GONE
    }

    private fun changeDescriptionToEdit() {
        editDescriptionButton.setImageDrawable(saveIconDrawable)
        textViewProfileDescription
        editDescriptionButton.setOnClickListener{
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
}
