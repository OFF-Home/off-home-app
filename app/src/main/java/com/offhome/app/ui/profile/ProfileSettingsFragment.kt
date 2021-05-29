package com.offhome.app.ui.profile



import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
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

        profileVM = ViewModelProviders.of(this).get(ProfileFragmentViewModel::class.java)

        emailTV = view.findViewById(R.id.emailUser2)
        usernameTV = view.findViewById(R.id.nameUser2)
        deleteAccount = view.findViewById(R.id.deleteAccount)
        btnChangePwd = view.findViewById(R.id.changePsw)
        btnNotifications = view.findViewById(R.id.imageViewIconNot)

        manageUserInfo()

        deleteAccount()

        changePassword()

        manageNotifications()
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
            builder.setCustomTitle(title)
            builder.setMessage("Are you sure you want to delete your account? This will permanently erase your account.")

            builder.setCancelable(true)
            builder.setPositiveButton("Delete") {
                _, _ ->
                val user = Firebase.auth.currentUser!!
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("POST", "User account deleted.")
                            profileVM.deleteAccount()
                        } else Toast.makeText(context, "There has been an error and the account has not been deleted", Toast.LENGTH_LONG).show()
                    }
                // FALTA CRIDA A BACK PER BORRAR EL USER DEL SERVER
                // després ha de portar a la pàgina del log in
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
}
