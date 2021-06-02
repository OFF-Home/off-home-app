package com.offhome.app.ui.otherprofile



import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.UserInfo
import com.offhome.app.ui.chats.singleChat.SingleChatActivity

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
    private lateinit var otherUser: UserInfo
    private lateinit var imageViewProfilePic: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var estrelles: RatingBar
    private lateinit var btnFollowFollowing: Button
    private lateinit var btnChat: FloatingActionButton
    private lateinit var fragment: AboutThemFragment

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // recibir user seleccionado de la otra pantalla. //robat de infoActivity
        // a la pantalla anterior ja hem d'haver fet l'acces a backend. perque hi necessitavem la fotoPerfil     (des de chats, des de la pagina d'una activity, ...)
        val arguments = intent.extras
        val otherUserString = arguments?.getString("user_info")
        otherUser = GsonBuilder().create().fromJson(otherUserString, UserInfo::class.java)

        textViewUsername = findViewById(R.id.otherUsername)
        textViewUsername.text = otherUser.username
        estrelles = findViewById(R.id.otherUserRatingBar)
        estrelles.rating = otherUser.estrelles.toFloat()
        btnFollowFollowing = findViewById(R.id.buttonFollow)
        imageViewProfilePic = findViewById(R.id.otherUserProfilePic)
        fragment =
            supportFragmentManager.findFragmentById(R.id.fragmentDinsOtherProfile) as AboutThemFragment

        viewModel = ViewModelProvider(this).get(OtherProfileViewModel::class.java) // funcionarà?

        Glide.with(applicationContext)
            .load(Constants().BASE_URL + "upload/userimageget/" + otherUser.username)
            .placeholder(R.drawable.profile_pic_placeholder).centerCrop().circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .into(imageViewProfilePic)

        viewModel.setUserInfo(otherUser)
        viewModel.isFollowing()

        btnFollowFollowing.setOnClickListener {
            if (btnFollowFollowing.text == getString(R.string.btn_follow))
                viewModel.follow().observe(this, {
                    if (it is Result.Success) {
                        viewModel.isFollowingValue.value = true
                        viewModel.updateFollowers(1)
                        changeFollowButtonText()
                    } else
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.error_follow),
                            Toast.LENGTH_LONG
                        ).show()
                })
            else viewModel.stopFollowing().observe(this, {
                if (it is Result.Success) {
                    viewModel.isFollowingValue.value = false
                    viewModel.updateFollowers(-1)
                    changeFollowButtonText()
                } else
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_follow),
                        Toast.LENGTH_LONG
                    ).show()
            })
        }

        btnChat = findViewById(R.id.floatingActionButton)
        if (otherUser.uid == SharedPreferenceManager.getStringValue(Constants().PREF_UID)) btnChat.visibility = View.GONE

        btnChat.setOnClickListener {
            val intent = Intent(this, SingleChatActivity::class.java)
            intent.putExtra("uid", otherUser.uid)
            intent.putExtra("username", otherUser.username)
            startActivity(intent)
        }

        observe()
    }

    /**
     * It observes the following list of one user and the response to the call of follow/unfollow
     */
    private fun observe() {
        viewModel.listFollowing.observe(
            this,
            {
                btnFollowFollowing.text = getString(R.string.btn_follow)
                if (it is Result.Success) {
                    for (item in it.data) {
                        if (item.usuariSeguidor == SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()) {
                            viewModel.setFollowing(true)
                            btnFollowFollowing.text = getString(R.string.btn_following)
                            break
                        }
                    }
                }

            }
        )
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
}
