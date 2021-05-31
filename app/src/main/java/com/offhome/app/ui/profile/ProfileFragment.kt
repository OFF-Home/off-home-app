package com.offhome.app.ui.profile



import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.ui.login.LoginActivity

/**
 * Class *ProfileFragment*
 *
 * Fragment for the Profile screen. On its ViewPager it can show either of the 3 fragments: MyActivities, AboutMe, Settings.
 * This class is one of the Views in this screen's MVVM's
 *
 * @author Ferran, Pau, others
 * @property fragmentViewModel reference to the ViewModel object
 * @property imageViewProfilePic reference to profile pic ImageView
 * @property textViewUsername reference to the username TextView
 * @property estrelles reference to the user's rating bar
 * @property editUsernameButton button to edit and save the username
 * @property constraintLayout1 reference to the layout's constraint layout
 * @property editIconDrawable drawable of the "edit" icon (a pencil)
 * @property saveIconDrawable drawable of the "save" icon (a diskette)
 * @property editTextUsername editText to edit the username
 * @property firebaseAuth is the gateway to the Firebase authentication API.
 */
class ProfileFragment : Fragment() {
    private lateinit var fragmentViewModel: ProfileFragmentViewModel
    lateinit var imageViewProfilePic: ImageView
    lateinit var textViewUsername: TextView
    lateinit var estrelles: RatingBar

    private lateinit var editUsernameButton: ImageView
    private lateinit var constraintLayout1: ConstraintLayout

    private lateinit var editIconDrawable: Drawable
    private lateinit var saveIconDrawable: Drawable
    private lateinit var editTextUsername: EditText

    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * Override the onCreateView method
     *
     * Does the fragment inflation
     * Initializes the attributes
     * Initializes the attributes that reference layout objects
     * Sets up the ViewPager and its tabs
     *
     * Makes the call to the VM to obtain the ProfileInfo and observes its live data for the result
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

        // cosas logout
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        firebaseAuth = Firebase.auth

        fragmentViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
        fragmentViewModel.getProfileInfo()
        fragmentViewModel.profileInfo.observe(
            viewLifecycleOwner,
            Observer {
                if (it is Result.Success) {
                    textViewUsername.text = it.data.username
                    estrelles.rating = it.data.estrelles.toFloat()
                    // imageViewProfilePic.setImageDrawable(/**/) // TODO la foto
                }
            }
        )

        iniEditElements()
        iniUsernameSetListener() // TODO sobra?

        imageViewProfilePic.setOnClickListener {
            // takePictureIntent()
            val selectPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context?.let { it1 ->
                ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE)
            }
                != PackageManager.PERMISSION_GRANTED
            ) {
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + requireContext().packageName)
                )
                startActivityForResult(selectPhoto, Constants().SELECT_PHOTO_GALLERY)
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants().PICK_PHOTO_FOR_AVATAR && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                val imageSelected = data.data
                val filepathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = requireContext().contentResolver.query(imageSelected!!, filepathColumn, null, null, null)
                if (cursor != null) {
                    cursor.moveToFirst()
                    val imageIndex: Int = cursor.getColumnIndex(filepathColumn[0])
                    val photoPath: String = cursor.getString(imageIndex)
                    fragmentViewModel.uploadPhoto(photoPath)
                    // Glide.with(this).load(photoPath).centerCrop().into(imageViewProfilePic)
                    Glide.with(this).load(photoPath).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageViewProfilePic)
                    cursor.close()
                }
            }
        }
    }

    /**
     * Initializes the listener that observes the call to backend made to edit the username
     *
     * the listener removes itself after one use
     */
    private fun iniUsernameSetListener() {
        //Log.d("PiniEditionResultListe", "arribo al Profile::iniEditionResultListeners")

        fragmentViewModel.usernameSetSuccessfullyResult.observe( // observer no salta. no sé perquè.
            viewLifecycleOwner,
            Observer {
                //Log.d("observer", "arribo al observer de fragmentViewModel.setUsernameSuccessfully1")
                val resultVM = it ?: return@Observer
                //Log.d("observer", "arribo al observer de fragmentViewModel.setUsernameSuccessfully2")

                if (resultVM is Result.Success) {
                    Toast.makeText(activity, R.string.username_updated_toast, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, R.string.username_update_error_toast, Toast.LENGTH_LONG).show()
                }

                // esborrem l'observer. Així, podem settejar-lo cada cop sense que s'acumulin
                fragmentViewModel.usernameSetSuccessfullyResult.removeObservers(viewLifecycleOwner) // hi ha una forma de treure només aquest observer, tipo removeObserver(this) pero nose com va
            }
        )
    }

    /**
     * ViewModel getter
     *
     * @return the profile fragment view model
     */
    fun getViewModel(): ProfileFragmentViewModel {
        return fragmentViewModel
    }

    /**
     * Initializes the edition elements:
     *
     * Initializes the editUsernameButton. It also sets its drawable and listener
     * Initializes the editTextUsername
     */
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

    /**
     * Initializes the editUsernameButton
     *
     * creates the object, sets id, initializes both drawables, inserts the View with its constraints in the constraint layout
     */
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

    /**
     * Initializes the username EditText
     *
     * creates the object, sets id, inserts the View with its constraints and size i the constraint layout
     * initializes its visibility to gone
     */
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

        // set the max chars
        val filterArray: Array<InputFilter> = arrayOf(InputFilter.LengthFilter(50))
        editTextUsername.filters = filterArray
    }

    /**
     * Changes the "state" of the username to editing
     *
     * meaning it changes the drawable to the "save" one, changes the listener, and changes the Username textView for the EditText in the layout
     */
    private fun changeUsernameToEdit() {
        editUsernameButton.setImageDrawable(saveIconDrawable)
        editUsernameButton.setOnClickListener {
            val newUsername = editTextUsername.text
            if (!newUsername.isEmpty()) {
                textViewUsername.text = newUsername
                fragmentViewModel.usernameChangedByUser(newUsername)
                iniUsernameSetListener()
                changeUsernameToDisplay()
            } else {
                Toast.makeText(activity, R.string.invalid_username, Toast.LENGTH_LONG).show()
            }
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

    /**
     * Changes the "state" of the username to display
     *
     * meaning it changes the drawable to the "edit" one, changes the listener, and changes the Username EditText for the textView in the layout
     */
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

    /**
     * Function to specify the options menu for an activity
     * @param menu provided
     * @param inflater the inflater
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Function called when the user selects an item from the options menu
     * @param item selected
     * @return true if the menu is successfully handled
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {

            val logout_dialog = AlertDialog.Builder(activity)
            logout_dialog.setTitle(R.string.dialog_logout_title)
            logout_dialog.setMessage(R.string.dialog_logout_message)
            logout_dialog.setPositiveButton(R.string.ok) { dialog, id ->
                firebaseAuth.signOut()
                SharedPreferenceManager.deleteData()
                requireActivity().run {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            logout_dialog.setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.dismiss()
            }
            logout_dialog.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
