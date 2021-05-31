package com.offhome.app.ui.updatePassword



import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
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
import com.offhome.app.MainActivity
import com.offhome.app.R
import com.offhome.app.ui.login.LoginActivity

/**
 * This class manage the change of the user's password in the settings of the profile inside our application
 * @author Maria Nievas Viñals
 * @property auth Declare the dependency for the Firebase Authentication library
 * @property btnAcceptChange references the Button to accept the change of password
 * @property text references the title of the layout of the recover password
 * @property firstText references the EditText to input the actual password of the user
 * @property secondText references the EditText to input the new password of the user
 * @property thirdText references the EditText to input the new password of the user repeated
 */
@Suppress("NAME_SHADOWING")
class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var btnAcceptChange: Button
    private lateinit var text: TextView
    private lateinit var firstText: EditText
    private lateinit var secondText: EditText
    private lateinit var thirdText: EditText

    /**
     * This function initializes the activity [UpdatePasswordActivity]
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setLayout()

        btnAcceptChange.setOnClickListener {
            changePassword()
        }
    }

    /**
     * This function sets the parameters of the view of the layout in order to let the user inputs the data
     * needed to change his password
     */
    @SuppressLint("SetTextI18n")
    private fun setLayout() {
        text = findViewById(R.id.textView)
        text.visibility = View.GONE
        firstText = findViewById(R.id.editTextEmailToRecover)
        firstText.hint = "current password"
        firstText.transformationMethod = PasswordTransformationMethod()
        secondText = findViewById(R.id.editTextNew)
        secondText.visibility = VISIBLE
        secondText.transformationMethod = PasswordTransformationMethod()
        thirdText = findViewById((R.id.editTextNewRepeat))
        thirdText.visibility = VISIBLE
        thirdText.transformationMethod = PasswordTransformationMethod()
        btnAcceptChange = findViewById(R.id.buttonRecoverPassword)
        btnAcceptChange.text = "Change Password"
    }

    /**
     * This function resets the parameters of the view of the layout in order to leave the view as originally
     */
    private fun resetLayout() {
        firstText.text.clear()
        secondText.text.clear()
        thirdText.text.clear()
        firstText.transformationMethod = null
        secondText.visibility = View.GONE
        thirdText.visibility = View.GONE
        text.visibility = VISIBLE
    }

    /**
     * This function manages the change of the password of the user logged and it checks whether the data given is correct
     */
    private fun changePassword() {
        if (firstText.text.isNotEmpty() && secondText.text.isNotEmpty() && thirdText.text.isNotEmpty()) {
            if (secondText.text.toString().trim().length > 5) {
                if (secondText.text.toString() == thirdText.text.toString()) {
                    val myUser = FirebaseAuth.getInstance().currentUser
                    if (myUser != null && myUser.email != null) {
                        val myUser = Firebase.auth.currentUser!!

                        // Get auth credentials from the user for re-authentication
                        val credential = EmailAuthProvider.getCredential(
                            myUser.email!!,
                            firstText.text.toString()
                        )

                        // Prompt the user to re-provide their sign-in credentials
                        myUser.reauthenticate(credential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Re-Authentication success",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val myUser = Firebase.auth.currentUser

                                myUser!!.updatePassword(secondText.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Password changed successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                            resetLayout()
                                        }
                                    }
                            } else Toast.makeText(
                                this,
                                "Re-Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                } else Toast.makeText(this, "Password mismatching", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "The password should be 5 characters at a minimum", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
    }
}
