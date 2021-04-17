package com.offhome.app.ui.recoverPassword

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R
import com.offhome.app.ui.login.LoginActivity
import com.offhome.app.ui.login.LoginViewModel
import com.offhome.app.ui.login.LoginViewModelFactory

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var btnRecover: Button
    private lateinit var editTextEmail: EditText

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

    private fun isValidEmail(): Boolean {
        return editTextEmail.text.contains('@') && Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text).matches()
    }
}