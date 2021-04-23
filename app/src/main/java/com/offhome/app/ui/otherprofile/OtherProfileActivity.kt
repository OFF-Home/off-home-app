package com.offhome.app.ui.otherprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.profile.UserInfo

class OtherProfileActivity : AppCompatActivity() {

    private lateinit var viewModel:OtherProfileViewModel
    private lateinit var otherUser:UserInfo
    private lateinit var imageViewProfilePic: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var estrelles: RatingBar
    private lateinit var btnFollowFollowing: Button
    private lateinit var fragment: AboutThemFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)

        //recibir user seleccionado de la otra pantalla. //robat de infoActivity
        //a la pantalla anterior ja hem d'haver fet l'acces a backend. perque hi necessitavem la fotoPerfil     (des de chats, des de la pagina d'una activity, ...)
        val arguments = intent.extras
        val otherUserString = arguments?.getString("user_info")
        otherUser = GsonBuilder().create().fromJson(otherUserString, UserInfo::class.java)
        imageViewProfilePic = findViewById(R.id.otherUserProfilePic)
        //imageViewProfilePic. //ficar-hi la imatge
        textViewUsername = findViewById(R.id.otherUsername)
        textViewUsername.text = otherUser.username
        estrelles = findViewById(R.id.otherUserRatingBar)
        estrelles.rating = otherUser.estrelles.toFloat()
        btnFollowFollowing = findViewById(R.id.buttonFollow)
        fragment = supportFragmentManager.findFragmentById(R.id.fragmentDinsOtherProfile) as AboutThemFragment

        viewModel = ViewModelProvider(this).get(OtherProfileViewModel::class.java)  //funcionarà?

        viewModel.setUserInfo(otherUser)
        viewModel.isFollowing()

        btnFollowFollowing.setOnClickListener {
            if (btnFollowFollowing.text == getString(R.string.btn_follow))
                viewModel.follow()
            else viewModel.stopFollowing()
        }

        observe()
    }

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
                if (item.usuariSeguidor == "currentUser") {
                    viewModel.setFollowing(true)
                    btnFollowFollowing.text = getString(R.string.btn_following)
                }
            }
        })
    }

    private fun changeFollowButtonText() {
        btnFollowFollowing.text = if (btnFollowFollowing.text == getString(R.string.btn_follow)) {
            getString(R.string.btn_following)
        } else {
            getString(R.string.btn_follow)
        }
        fragment.updateFollowes()
    }
}