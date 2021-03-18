package com.offhome.app.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.MainActivity
import com.offhome.app.R

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "OFF Home"

        setContentView(R.layout.activity_login)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnShowPassword = findViewById<ImageView>(R.id.imageViewShowPassword)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        // val btnToSignUp = findViewById<TextView>(R.id.textViewHere)
        // val btnLoginGoogle = findViewById<Button>(R.id.buttonGoogleLogin)
        loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(
            this@LoginActivity,
            Observer {
                val loginState = it ?: return@Observer

                // disable login button unless both username / password is valid
                btnLogin.isEnabled = loginState.isDataValid

                if (loginState.emailError != null) {
                    editTextEmail.error = getString(loginState.emailError)
                }
                if (loginState.passwordError != null) {
                    editTextPassword.error = getString(loginState.passwordError)
                }
            }
        )

        loginViewModel.loginResult.observe(
            this@LoginActivity,
            Observer {
                val loginResult = it ?: return@Observer

                if (loginResult.error != null) {
                    loading.visibility = View.GONE
                    showLoginFailed(loginResult.error)
                }
                if (loginResult.success != null) {
                    updateUiWithUser(loginResult.success)
                }
                setResult(Activity.RESULT_OK)

                // Complete and destroy login activity once successful
                // finish()
            }
        )

        editTextEmail.afterTextChanged {
            loginViewModel.loginDataChanged(
                editTextEmail.text.toString(),
                editTextPassword.text.toString()
            )
        }

        editTextPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            editTextEmail.text.toString(),
                            editTextPassword.text.toString()
                        )
                }
                false
            }

            btnLogin.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
            }
        }

        btnShowPassword.setOnClickListener {
            Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT).show()
            editTextPassword.inputType = if (editTextPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editTextPassword.setSelection(editTextPassword.text.length)
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        model.data.observe(
            this@LoginActivity,
            {
                loading.visibility = View.GONE
                val welcome = getString(R.string.welcome)
                when {
                    it.errorLogin != null && it.errorLogin == R.string.login_failed_email -> Toast.makeText(applicationContext, getString(R.string.login_failed_email), Toast.LENGTH_LONG).show()
                    it.errorLogin != null && it.errorLogin == R.string.login_failed_login -> Toast.makeText(applicationContext, getString(R.string.login_failed_login), Toast.LENGTH_LONG).show()
                    else -> {
                        val displayName = it.displayUsername
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            applicationContext,
                            "$welcome $displayName",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        )
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
