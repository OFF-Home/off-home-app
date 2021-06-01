package com.offhome.app.ui.profile


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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
                    Glide.with(requireContext())
                        .load(Constants().BASE_URL + "upload/userimageget/" + it.data.username)
                        .placeholder(R.drawable.profile_pic_placeholder).centerCrop().circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(imageViewProfilePic)
                }
            }
        )

        updateProfilePic()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants().PICK_PHOTO_FOR_AVATAR && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                val imageSelected = data.data
                val filepathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = requireContext().contentResolver.query(
                    imageSelected!!,
                    filepathColumn,
                    null,
                    null,
                    null
                )
                if (cursor != null) {
                    cursor.moveToFirst()
                    val imageIndex: Int = cursor.getColumnIndex(filepathColumn[0])
                    val photoPath: String = cursor.getString(imageIndex)
                    fragmentViewModel.uploadPhoto(photoPath).observe(
                        viewLifecycleOwner, { it ->
                            if (it is Result.Success) {
                                // Glide.with(this).load(photoPath).centerCrop().into(imageViewProfilePic)
                                Glide.with(this).load(photoPath).centerCrop().circleCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .into(imageViewProfilePic)
                                cursor.close()
                            } else if (it is Result.Error) {
                                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    )
                }
            }
        }
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
                SharedPreferenceManager.setBooleanValue(
                    Constants().PREF_IS_NOT_FIRST_TIME_OPENING_APP,
                    true
                )
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

    private fun updateProfilePic() {
        imageViewProfilePic.setOnClickListener {
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, Constants().SELECT_PHOTO_GALLERY)
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }
    }
}
