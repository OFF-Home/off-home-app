package com.offhome.app.ui.otherprofile

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.profile.UserInfo
import java.io.ByteArrayOutputStream

class OtherProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: OtherProfileViewModel
    private lateinit var otherUser: UserInfo
    private lateinit var imageViewProfilePic: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var estrelles: RatingBar

    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageUri : Uri

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
            takePictureIntent()
        }

        textViewUsername = findViewById(R.id.otherUsername)
        textViewUsername.text = otherUser.username
        estrelles = findViewById(R.id.otherUserRatingBar)
        estrelles.rating = otherUser.estrelles.toFloat()

        viewModel = ViewModelProvider(this).get(OtherProfileViewModel::class.java) // funcionarà?

        viewModel.setUserInfo(otherUser)
    }

    private fun takePictureIntent(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "The camara is not available", Toast.LENGTH_LONG).show()
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
        val constraintLayout = findViewById<ConstraintLayout>(R.id.otherProfileConstraintLayout)
        constraintLayout.visibility = View.VISIBLE
        upload.addOnCompleteListener{ uploadTask ->
            if(uploadTask.isSuccessful){
                constraintLayout.visibility = View.INVISIBLE
                storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show()
                        //OtherProfileActivity?.toast(imageUri.toString())
                        imageViewProfilePic.setImageBitmap(bitmap)
                    }
                }
            }
            else{
                uploadTask.exception?.let {
                    Toast.makeText(this, it.message!!, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}