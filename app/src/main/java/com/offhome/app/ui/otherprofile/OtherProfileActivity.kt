package com.offhome.app.ui.otherprofile

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.profile.UserInfo

class OtherProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: OtherProfileViewModel
    private lateinit var otherUser: UserInfo
    private lateinit var imageViewProfilePic: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var estrelles: RatingBar

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
        textViewUsername = findViewById(R.id.otherUsername)
        textViewUsername.text = otherUser.username
        estrelles = findViewById(R.id.otherUserRatingBar)
        estrelles.rating = otherUser.estrelles.toFloat()

        viewModel = ViewModelProvider(this).get(OtherProfileViewModel::class.java) // funcionarà?

        viewModel.setUserInfo(otherUser)
    }
}
