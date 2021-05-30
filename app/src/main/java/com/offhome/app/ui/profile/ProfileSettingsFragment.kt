package com.offhome.app.ui.profile



import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.ui.infopolitiques.CovidPolicyActivity
import com.offhome.app.ui.infopolitiques.InfoOFFHomeActivity
import com.offhome.app.ui.infopolitiques.PolicyActivity
import com.offhome.app.ui.login.LoginActivity
import com.offhome.app.ui.updatePassword.UpdatePasswordActivity

/**
 * Class *ProfileSettingsFragment*
 *
 * Fragment for the "Settings" section (page) of the Profile screen.
 * This class is one of the Views in this screen's MVVM's
 *
 * @author Maria
 *
 */
@Suppress("DEPRECATION")
class ProfileSettingsFragment : Fragment() {

    lateinit var usernameTV: TextView
    lateinit var emailTV: TextView
    lateinit var deleteAccount: TextView
    lateinit var btnChangePwd: TextView
    lateinit var btnNotifications: ImageView
    lateinit var btnInfoOFFHOME: TextView
    lateinit var btnPrivacyPolicy: TextView
    lateinit var btnCovidPolicy: TextView
    lateinit var btnDarkMode: ImageView

    private var dark_mode: Boolean = false
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var profileVM: ProfileFragmentViewModel

    /**
     * Override the onCreateView method
     *
     * Does the fragment inflation
     * Initializes the attributes
     * Initializes the attributes that reference layout objects
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

        super.onCreate(savedInstanceState)
        firebaseAuth = Firebase.auth
        return inflater.inflate(R.layout.profile_settings_fragment, container, false)
    }

    /**
     * /The layout view has been created and it is already available
     * @param view The View returned by the method onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here. This value may be null
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileFragment: ProfileFragment = parentFragment as ProfileFragment
        profileVM = profileFragment.getViewModel()

        emailTV = view.findViewById(R.id.emailUser2)
        usernameTV = view.findViewById(R.id.nameUser2)
        deleteAccount = view.findViewById(R.id.deleteAccount)
        btnChangePwd = view.findViewById(R.id.changePsw)
        btnNotifications = view.findViewById(R.id.imageViewIconNot)
        btnDarkMode = view.findViewById(R.id.imageViewIconDark)

        dark_mode = SharedPreferenceManager.getBooleanValue(Constants().DARK_MODE.toString())
        btnInfoOFFHOME = view.findViewById(R.id.aboutOFFHome)
        btnPrivacyPolicy = view.findViewById(R.id.privacyPolicy)
        btnCovidPolicy = view.findViewById(R.id.COVIDPolicy)

        manageUserInfo()

        deleteAccount()

        changePassword()

        manageNotifications()

        infoOFFHOME()

        privacyPolicy()

        covidPolicy()

        changeToDarkMode()
    }

    private fun covidPolicy() {
        btnCovidPolicy.setOnClickListener {
            startActivity(Intent(activity, CovidPolicyActivity::class.java))
        }
    }

    private fun privacyPolicy() {
        btnPrivacyPolicy.setOnClickListener {
            startActivity(Intent(activity, PolicyActivity::class.java))
        }
    }

    private fun infoOFFHOME() {
        btnInfoOFFHOME.setOnClickListener {
            startActivity(Intent(activity, InfoOFFHomeActivity::class.java))
        }
    }

    /**
     * This function inicializes the user information - the user name and his/her email - in the Settings screen inside the app profile
     */
    private fun manageUserInfo() {
        emailTV.text = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)
        emailTV.setTextColor(Color.LTGRAY)

        usernameTV.text = SharedPreferenceManager.getStringValue(Constants().PREF_USERNAME)
        usernameTV.setTextColor(Color.LTGRAY)
    }

    /**
     * This function manages the deletion of the user account with a click from the screen to the TextView deleteAccount.
     * It also calls the Firebase to delete the user from there and the viewModel to send the info to the back.
     */
    @SuppressLint("SetTextI18n")
    private fun deleteAccount() {
        deleteAccount.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val title = TextView(context)
            title.text = "Delete your account"
            title.setPadding(10, 10, 10, 10)
            title.gravity = CENTER
            title.textSize = 18F
            val user = Firebase.auth.currentUser!!

            builder.setCustomTitle(title)
            builder.setMessage("Are you sure you want to delete your account? This will permanently erase your account.")
            builder.setCancelable(true)
            builder.setPositiveButton("Delete") { _, _ ->
                // val progress = ProgressDialog.show(context, "Loading", "Please wait...", true)
                profileVM.deleteAccount().observe(
                    viewLifecycleOwner,
                    { sth ->
                        if (sth is Result.Success) {
                            user.delete()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("POST", "User account deleted")
                                        Toast.makeText(
                                            context,
                                            "User account deleted",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        // retornar a la pàgina de log in
                                        SharedPreferenceManager.deleteData()
                                        requireActivity().run {
                                            startActivity(Intent(this, LoginActivity::class.java))
                                            finish()
                                        }
                                    }
                                }
                        } else if (sth is Result.Error) {
                            Toast.makeText(context, sth.exception.message, Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
    }

    /**
     * This function manages the change of the user's password.
     */
    private fun changePassword() {
        btnChangePwd.setOnClickListener {
            if (SharedPreferenceManager.getStringValue(Constants().PREF_PROVIDER) == Constants().PREF_PROVIDER_PASSWORD)
                requireActivity().run {
                    startActivity(Intent(this, UpdatePasswordActivity::class.java))
                    finish()
                }
            else
                Toast.makeText(
                    context,
                    getString(R.string.error_change_password_google),
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    /**
     * This function manages the Notifications preferences (on / off)
     */
    private fun manageNotifications() {
        var clicked = false
        btnNotifications.setOnClickListener {
            clicked = !clicked
            if (clicked) {
                btnNotifications.setImageResource(R.drawable.ic_outline_notifications_active_24)
                Toast.makeText(context, "Notifications disabled", Toast.LENGTH_SHORT).show()
                // crida a Back
            } else {
                btnNotifications.setImageResource(R.drawable.ic_baseline_notifications_active_24)
                Toast.makeText(context, "Notifications enabled", Toast.LENGTH_SHORT).show()
                // crida a Back
            }
        }
    }

    private fun changeToDarkMode(){
        btnDarkMode.setOnClickListener{
            if (dark_mode) setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            else setDefaultNightMode(MODE_NIGHT_YES)
            dark_mode = !dark_mode
        }
    }
}
