package com.offhome.app.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.ui.updatePassword.UpdatePasswordActivity

class ProfileSettingsFragment: Fragment() {

    lateinit var darkOFFHome : TextView
    lateinit var usernameTV : TextView
    lateinit var emailTV : TextView

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

        emailTV = view.findViewById(R.id.emailUser2)
        emailTV.text = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)
        emailTV.setTextColor(Color.LTGRAY)

        usernameTV = view.findViewById(R.id.nameUser2)
        usernameTV.text = SharedPreferenceManager.getStringValue(Constants().PREF_USERNAME)
        usernameTV.setTextColor(Color.LTGRAY)

        val btnChangePwd = view.findViewById<TextView>(R.id.changePsw)

        btnChangePwd.setOnClickListener {
            if (SharedPreferenceManager.getStringValue(Constants().PREF_PROVIDER) == Constants().PREF_PROVIDER_PASSWORD)
                requireActivity().run {
                    startActivity(Intent(this, UpdatePasswordActivity::class.java))
                    finish()
                }
            else
                Toast.makeText(context, getString(R.string.error_change_password_google), Toast.LENGTH_LONG).show()
        }
    }

}
