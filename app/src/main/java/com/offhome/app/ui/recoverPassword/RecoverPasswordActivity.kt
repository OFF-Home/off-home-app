package com.offhome.app.ui.recoverPassword

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R
import com.offhome.app.ui.login.LoginActivity
import com.offhome.app.ui.login.LoginViewModel
import com.offhome.app.ui.login.LoginViewModelFactory

/**
 * Class *RecoverPasswordActivity*
 *
 * This class is the one that interacts with the User to reset the password through a email
 * @author Pau Cuesta Arcos
 * @property viewModel references the ViewModel class for this Activity
 * @property btnRecover references the Button to proceed the email sending
 * @property editTextEmail references the EditText to input the email of the user
 */
class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var btnRecover: Button
    private lateinit var editTextEmail: EditText

    /**
     * This function is automatically called when creating the activity
     * It contains the clicks and the observer of the result of the invocation to the call to send
     * the email
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnRecover = findViewById(R.id.buttonRecoverPassword)
        editTextEmail = findViewById(R.id.editTextEmailToRecover)

        viewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        btnRecover.setOnClickListener {
            if (isValidEmail()) {
                viewModel.recoverPassword(editTextEmail.text.toString())
            } else {
                editTextEmail.setBackgroundResource(R.drawable.background_edit_text_wrong)
                Toast.makeText(this, "Email not correct!", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.recoverResult.observe(
            this@RecoverPasswordActivity,
            Observer {
                val recoverResult = it ?: return@Observer
                Toast.makeText(applicationContext, "Enters $recoverResult", Toast.LENGTH_LONG).show()
                if (recoverResult == "OK") {
                    Toast.makeText(applicationContext, "Email sent. Please check your email inbox", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else if (recoverResult == "ERROR") {
                    editTextEmail.setBackgroundResource(R.drawable.background_edit_text_wrong)
                    Toast.makeText(this, "Email not found!", Toast.LENGTH_LONG).show()
                }

                // Complete and destroy login activity once successful
                // finish()
            }
        )
    }

    /**
     * It checks if it satisfies the pattens of an email
     */
    private fun isValidEmail(): Boolean {
        return editTextEmail.text.contains('@') && Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text).matches()
    }
}
