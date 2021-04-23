package com.offhome.app.ui.updatePassword

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R
import com.offhome.app.databinding.ActivitiesActivityBinding.inflate
import com.offhome.app.databinding.ActivityActivitiesListBinding.inflate
import com.offhome.app.ui.profile.ProfileFragmentViewModel

class UpdatePasswordActivity: AppCompatActivity() {

    private lateinit var btnAcceptChange : Button
    private lateinit var text : TextView
    private lateinit var firstText : EditText
    private lateinit var secondText : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setLayout()

        btnAcceptChange.setOnClickListener {

        }
    }

    private fun setLayout(){
        secondText = findViewById(R.id.editTextNew)
        secondText.visibility = VISIBLE;
        firstText = findViewById(R.id.editTextEmailToRecover)
        text = findViewById(R.id.textView)
        text.visibility = INVISIBLE;
        btnAcceptChange = findViewById(R.id.buttonRecoverPassword)
        firstText.hint = "Current password"
        btnAcceptChange.text = "Change Password"
    }
}


    /*
    fragmentViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
    fragmentViewModel.getProfileInfo()
    fragmentViewModel.profileInfo.observe(
        viewLifecycleOwner,
        Observer { // aquest observer no arriba a executar-se però el de AboutMeFragment sí. NO ENTENC PERQUÈ
            val profileInfoVM = it ?: return@Observer
            username = profileInfoVM.username
        }
    )*/
