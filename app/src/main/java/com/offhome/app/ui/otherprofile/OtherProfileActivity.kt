package com.offhome.app.ui.otherprofile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.profile.UserInfo
import java.io.ByteArrayOutputStream


/**
 * Class *OtherProfileActivity*
 *
 * Activity of the OtherProfile screen. Its layout contains a fragment where the AboutThemFragment is set
 * This class is one of the Views in this screen's MVVM's
 *
 * @author Pau, Ferran
 * @property viewModel reference to the ViewModel object
 * @property imageViewProfilePic reference to profile pic ImageView
 * @property textViewUsername reference to the username TextView
 * @property estrelles reference to the user's rating bar
 * @property btnFollowFollowing reference to the follow/following button
 * @property fragment reference to the fragment inside this activity which will contain the AboutThemFragment
 * @property otherUser user's user info
 */
class OtherProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: OtherProfileViewModel
    private lateinit var imageViewProfilePic: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var estrelles: RatingBar
    private lateinit var btnFollowFollowing: Button
    private lateinit var fragment: AboutThemFragment
    private lateinit var otherUser: UserInfo

    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageUri : Uri
    val PICK_PHOTO_FOR_AVATAR = 1
    val SELECT_PHOTO_GALLERY = 1

    /**
     * Override the onCreate method
     *
     * initializes the otherUser with the data received from the previous Activity
     * Initializes the layout elements
     * Initializes the attributes
     * Sets the btnFollowFollowing's listener
     *
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)

        // recibir user seleccionado de la otra pantalla. //robat de infoActivity
        // a la pantalla anterior ja hem d'haver fet l'acces a backend. perque hi necessitavem la fotoPerfil     (des de chats, des de la pagina d'una activity, ...)
        val arguments = intent.extras
        val otherUserString = arguments?.getString("user_info")
        otherUser = GsonBuilder().create().fromJson(otherUserString, UserInfo::class.java)
        imageViewProfilePic = findViewById(R.id.otherUserProfilePic)

        // imageViewProfilePic. //ficar-hi la imatge
        imageViewProfilePic.setOnClickListener {
            //takePictureIntent()
            val selectPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(selectPhoto, SELECT_PHOTO_GALLERY)
        }

        textViewUsername = findViewById(R.id.otherUsername)
        textViewUsername.text = otherUser.username
        estrelles = findViewById(R.id.otherUserRatingBar)
        estrelles.rating = otherUser.estrelles.toFloat()
        btnFollowFollowing = findViewById(R.id.buttonFollow)
        fragment = supportFragmentManager.findFragmentById(R.id.fragmentDinsOtherProfile) as AboutThemFragment

        viewModel = ViewModelProvider(this).get(OtherProfileViewModel::class.java) // funcionarà?

        viewModel.setUserInfo(otherUser)
        viewModel.isFollowing()

        btnFollowFollowing.setOnClickListener {
            if (btnFollowFollowing.text == getString(R.string.btn_follow))
                viewModel.follow()
            else viewModel.stopFollowing()
        }

        observe()
    }

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
            finish()
            startActivity(intent)
            return
        }*/

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "The camara is not available", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * It observes the following list of one user and the response to the call of follow/unfollow
     */
    private fun observe() {
        viewModel.followResult.observe(this, {
            if (it)
                changeFollowButtonText()
            else
                Toast.makeText(applicationContext, getString(R.string.error_follow), Toast.LENGTH_LONG).show()
        })

        viewModel.listFollowing.observe(this, {
            btnFollowFollowing.text = getString(R.string.btn_follow)
            for (item in it) {
                if (item.usuariSeguidor == SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()) {
                    viewModel.setFollowing(true)
                    btnFollowFollowing.text = getString(R.string.btn_following)
                }
            }
        })
    }

    /**
     * Change the button text from Follow to Following and viceversa
     */
    private fun changeFollowButtonText() {
        btnFollowFollowing.text = if (btnFollowFollowing.text == getString(R.string.btn_follow)) {
            getString(R.string.btn_following)
        } else {
            getString(R.string.btn_follow)
        }
        fragment.updateFollowes()
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK) {
            if (data != null) {
                val imageSelected = data.data
                val filepathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    contentResolver.query(imageSelected!!, filepathColumn, null, null, null)
                if (cursor != null) {
                    cursor.moveToFirst()
                    val imageIndex: Int = cursor.getColumnIndex(filepathColumn[0])
                    val photoPath: String = cursor.getString(imageIndex)
                    viewModel.uploadPhoto(photoPath)
                    cursor.close()
                }
            }
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.otherProfileConstraintLayout)
        constraintLayout.visibility = View.VISIBLE
        upload.addOnCompleteListener{ uploadTask ->
            if(uploadTask.isSuccessful){
                constraintLayout.visibility = View.INVISIBLE
                storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                    urlTask.result.let {
                        if (it != null) {
                            imageUri = it
                        }
                        Toast.makeText(this, "Imatge actualitzada correctament", Toast.LENGTH_LONG).show()
                        imageViewProfilePic.setImageBitmap(bitmap)
                    }
                }
            }
            else{
                uploadTask.exception.let {
                    if (it != null) {
                        Toast.makeText(this, it.message!!, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}
