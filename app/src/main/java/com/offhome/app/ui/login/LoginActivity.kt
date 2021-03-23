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
import com.offhome.app.ui.signup.SignUpActivity

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
        val btnToSignUp = findViewById<TextView>(R.id.textViewHere)
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

                if (editTextEmail.text.isNotEmpty() && loginState.emailError != null) {
                    editTextEmail.setBackgroundResource(R.drawable.background_edit_text_wrong)
                    Toast.makeText(this, getString(loginState.emailError), Toast.LENGTH_LONG).show()
                }
                if (editTextPassword.text.isNotEmpty() && loginState.passwordError != null) {
                    editTextPassword.setBackgroundResource(R.drawable.background_edit_text_wrong)
                    Toast.makeText(this, getString(loginState.passwordError), Toast.LENGTH_LONG).show()
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
            editTextEmail.setBackgroundResource(R.drawable.background_edit_text)
        }

        editTextEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                loginViewModel.loginDataChanged(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
        }

        editTextPassword.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    loginViewModel.loginDataChanged(
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    )
            }

            afterTextChanged {
                setBackgroundResource(R.drawable.background_edit_text)
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
            editTextPassword.inputType = if (editTextPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editTextPassword.setSelection(editTextPassword.text.length)
        }

        btnToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
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
