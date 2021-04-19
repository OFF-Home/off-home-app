package com.offhome.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.ui.otherprofile.OtherProfileActivity
import java.io.ByteArrayOutputStream

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
    private lateinit var constraintLayout1: ConstraintLayout

    private lateinit var editIconDrawable: Drawable
    private lateinit var saveIconDrawable: Drawable
    private lateinit var editTextUsername: EditText

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri : Uri

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
        constraintLayout1 = view.findViewById(R.id.profileConstraintLayoutDinsAppBarLO)

        val sectionsPagerAdapter = SectionsPagerAdapter(inflater.context, childFragmentManager)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        fragmentViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
        fragmentViewModel.getProfileInfo()
        fragmentViewModel.profileInfo.observe(
            viewLifecycleOwner,
            Observer { // aquest observer no arriba a executar-se però el de AboutMeFragment sí. NO ENTENC PERQUÈ
                val profileInfoVM = it ?: return@Observer

                textViewUsername.text = profileInfoVM.username
                estrelles.rating = profileInfoVM.estrelles.toFloat()
                // imageViewProfilePic.setImageDrawable(/**/) // TODO la foto
            }
        )

        iniEditElements()

        imageViewProfilePic.setOnClickListener {
            // TODO aqui no anirà això. ho he posat per a testejar el canvi a OtherProfile, d'una altra HU. (Ferran)
            canviAOtherProfile()
        }

        return view
    }

    fun getViewModel(): ProfileFragmentViewModel {
        return fragmentViewModel
    }

    private fun iniEditElements() {
        iniEditUsernameButton()

        // we set our new scaled drawable
        editUsernameButton.setImageDrawable(editIconDrawable)

        editUsernameButton.setOnClickListener {
            changeUsernameToEdit()
        }
        iniEditTextUsername()

        // fer iniEditProfilePicButton aquí
    }

    private fun iniEditUsernameButton() {
        editUsernameButton = ImageView(activity)
        editUsernameButton.id = R.id.editUsernameButton

        // TODO codi repetit de ProfileAboutMeFragment. fer algo?

        // to resize the drawable, we create a local drawable here
        val dr: Drawable = resources.getDrawable(android.R.drawable.ic_menu_edit)
        val bitmap: Bitmap = (dr as BitmapDrawable).bitmap
        // we scale it
        editIconDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 70, 70, true))
        // we prepare the saveIconDrawable, resizing it
        val dr2: Drawable = resources.getDrawable(android.R.drawable.ic_menu_save)
        val bitmap2: Bitmap = (dr2 as BitmapDrawable).bitmap
        // we scale it
        saveIconDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap2, 70, 70, true))

        constraintLayout1.addView(editUsernameButton)

        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout1)
        constraintSet1.connect(R.id.editUsernameButton, ConstraintSet.LEFT, R.id.textViewUsername, ConstraintSet.RIGHT, 8)
        constraintSet1.connect(R.id.editUsernameButton, ConstraintSet.TOP, R.id.textViewUsername, ConstraintSet.TOP)
        constraintSet1.applyTo(constraintLayout1)
    }

    private fun iniEditTextUsername() {
        editTextUsername = EditText(activity)
        editTextUsername.id = R.id.editTextUsername2 // li he dit 2 perquè ja existia un editTextUsername aparentment

        constraintLayout1.addView(editTextUsername)
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout1)
        constraintSet1.connect(R.id.editTextUsername2, ConstraintSet.LEFT, R.id.profileConstraintLayoutDinsAppBarLO, ConstraintSet.LEFT)
        constraintSet1.connect(R.id.editTextUsername2, ConstraintSet.RIGHT, R.id.profileConstraintLayoutDinsAppBarLO, ConstraintSet.RIGHT)
        constraintSet1.connect(R.id.editTextUsername2, ConstraintSet.TOP, R.id.imageViewProfilePic, ConstraintSet.BOTTOM)
        // falta clear?
        constraintSet1.connect(R.id.textViewUsername, ConstraintSet.TOP, R.id.editTextUsername2, ConstraintSet.BOTTOM)

        constraintSet1.applyTo(constraintLayout1)

        val editTextlayoutParams: ViewGroup.LayoutParams = editTextUsername.layoutParams
        editTextlayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT

        editTextUsername.visibility = View.GONE
    }

    private fun changeUsernameToEdit() {
        editUsernameButton.setImageDrawable(saveIconDrawable)
        editUsernameButton.setOnClickListener {
            textViewUsername.text = editTextUsername.text
            fragmentViewModel.usernameChangedByUser(editTextUsername.text)
            changeUsernameToDisplay()
        }
        editTextUsername.setText(textViewUsername.text)
        editTextUsername.setHint(R.string.hint_username)

        editUsernameButton
        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout1)
        constraintSet1.connect(R.id.editUsernameButton, ConstraintSet.LEFT, R.id.editTextUsername2, ConstraintSet.RIGHT, 8)
        constraintSet1.connect(R.id.editUsernameButton, ConstraintSet.TOP, R.id.editTextUsername2, ConstraintSet.TOP)
        constraintSet1.connect(R.id.editUsernameButton, ConstraintSet.BOTTOM, R.id.editTextUsername2, ConstraintSet.BOTTOM)
        constraintSet1.applyTo(constraintLayout1)

        editTextUsername.visibility = View.VISIBLE
        textViewUsername.visibility = View.GONE
    }

    private fun changeUsernameToDisplay() {
        editUsernameButton.setImageDrawable(editIconDrawable)
        editUsernameButton.setOnClickListener {
            changeUsernameToEdit()
        }

        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(constraintLayout1)
        constraintSet1.connect(R.id.editUsernameButton, ConstraintSet.LEFT, R.id.textViewUsername, ConstraintSet.RIGHT, 8)
        constraintSet1.connect(R.id.editUsernameButton, ConstraintSet.TOP, R.id.textViewUsername, ConstraintSet.TOP)
        constraintSet1.clear(R.id.editUsernameButton, ConstraintSet.BOTTOM)
        constraintSet1.applyTo(constraintLayout1)

        textViewUsername.visibility = View.VISIBLE
        editTextUsername.visibility = View.GONE
    }

    // aixo es completament per a testejar
    private fun canviAOtherProfile() {

        // stub
        val userInfo = com.offhome.app.model.profile.UserInfo(
            email = "yesThisIsVictor@gmail.com", username = "victorfer", password = "1234", birthDate = "12-12-2012",
            description = "Lou Spence (1917–1950) was a fighter pilot and squadron commander in the Royal Australian Air Force during World War II and the Korean War. In 1941 he was posted to North Africa with No. 3 Squadron, which operated P-40 Tomahawks and Kittyhawks; he was credited with shooting down two German aircraft and earned the Distinguished Flying Cross (DFC). He commanded No. 452 Squadron in ",
            followers = 200, following = 90, darkmode = 0, notifications = 0, estrelles = 3, tags = "a b c d e", language = "esp"
        )

        val intentCanviAOtherProfile = Intent(context, OtherProfileActivity::class.java) // .apply {        }
        intentCanviAOtherProfile.putExtra("user_info", GsonBuilder().create().toJson(userInfo))
        startActivity(intentCanviAOtherProfile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViewProfilePic.setOnClickListener {
            takePictureIntent()
        }
    }

    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ pictureIntent ->
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also{
                startActivityForResult(pictureIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        constraintLayout1.visibility = View.VISIBLE
        upload.addOnCompleteListener{ uploadTask ->
            if(uploadTask.isSuccessful){
                constraintLayout1.visibility = View.INVISIBLE
                storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        //   activity?.toast(imageUri.toString())
                        imageViewProfilePic.setImageBitmap(bitmap)
                    }
                }
            }
            else{
                uploadTask.exception?.let {
                    // activity?.toast(it.message!!)
                }
            }
        }
    }

}
