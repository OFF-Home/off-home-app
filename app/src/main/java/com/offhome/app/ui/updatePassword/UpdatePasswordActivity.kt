package com.offhome.app.ui.updatePassword

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.R
import com.offhome.app.ui.login.LoginActivity


class UpdatePasswordActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var btnAcceptChange : Button
    private lateinit var text : TextView
    private lateinit var firstText : EditText
    private lateinit var secondText : EditText
    private lateinit var thirdText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setLayout()

        btnAcceptChange.setOnClickListener {
            changePassword()
            resetLayout()
        }
    }

    private fun setLayout(){
        text = findViewById(R.id.textView)
        text.visibility = INVISIBLE;
        firstText = findViewById(R.id.editTextEmailToRecover)
        firstText.hint = "current password"
        firstText.transformationMethod = PasswordTransformationMethod()
        secondText = findViewById(R.id.editTextNew)
        secondText.visibility = VISIBLE;
        secondText.transformationMethod = PasswordTransformationMethod()
        thirdText = findViewById((R.id.editTextNewRepeat))
        thirdText.visibility = VISIBLE
        thirdText.transformationMethod = PasswordTransformationMethod()
        btnAcceptChange = findViewById(R.id.buttonRecoverPassword)
        btnAcceptChange.text = "Change Password"
    }

    private fun resetLayout(){
        text.visibility = VISIBLE;
        firstText.transformationMethod = null
        secondText.visibility = INVISIBLE;
        thirdText.visibility = INVISIBLE
    }

    private fun changePassword(){
        if (firstText.text.isNotEmpty() && secondText.text.isNotEmpty() && thirdText.text.isNotEmpty()){
            if (secondText.text.toString() == thirdText.text.toString()){
                val myUser = auth.currentUser
                if (myUser != null && myUser.email != null){
                    val myUser = Firebase.auth.currentUser!!

                    // Get auth credentials from the user for re-authentication. The example below shows
                    // email and password credentials but there are multiple possible providers,
                    // such as GoogleAuthProvider or FacebookAuthProvider.
                    val credential = EmailAuthProvider.getCredential(
                        myUser.email!!,
                        firstText.text.toString()
                    )

                    // Prompt the user to re-provide their sign-in credentials
                    myUser.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this, "Re-Authentication success", Toast.LENGTH_SHORT).show()
                            val myUser = Firebase.auth.currentUser

                            myUser!!.updatePassword(secondText.text.toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Password changed successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                       // startActivity(Intent(this,MainActivity::class.java))
                                        //finish()
                                    }
                                }
                        }
                        else Toast.makeText(this, "Re-Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            else Toast.makeText(this, "Password mismatching", Toast.LENGTH_SHORT).show()
        }
        else Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
    }
}
