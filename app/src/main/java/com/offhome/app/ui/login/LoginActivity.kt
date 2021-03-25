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

/**
 * Class *LoginActicity*
 *
 * This class is the one that interacts with the User
 * @author Pau Cuesta Arcos
 * @property loginViewModel references the ViewModel class for this Activity
 * @property loading references the ProgressBar shown while doing the login in background until receiving a response
 * @property editTextEmail references the EditText to input the email of the user
 * @property editTextPassword references the EditText to input the password of the user
 * @property btnLogin references the Button to do the login to the app
 * @property btnToSignUp references the TextView that allows the user to navigate to another activity to SignUp
 * @property btnLoginGoogle references the Button to login to the app through Google Sign In
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loading: ProgressBar
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnShowPassword: ImageView
    private lateinit var btnLogin: Button
    private lateinit var btnToSignUp: TextView
    private lateinit var btnLoginGoogle: Button

    /**
     * It is executed when the activity is launched for first time or created again following
     * activities lifecycle.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUp()
        startObservers()
        editTextsChanges()

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

    /**
     * It set's the title of the activity and it binds all the components from the .xml file
     * to the activity to be able to deal with them
     */
    private fun setUp() {
        title = "OFF Home"
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnShowPassword = findViewById(R.id.imageViewShowPassword)
        btnLogin = findViewById(R.id.buttonLogin)
        btnToSignUp = findViewById(R.id.textViewHere)
        btnLoginGoogle = findViewById(R.id.buttonGoogleLogin)
        loading = findViewById(R.id.loading)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
    }

    /**
     * It starts the observers of the state of the login and the login result. In case of changes,
     * it takes the necessary actions
     */
    private fun startObservers() {
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
                    // editTextPassword.setBackgroundResource(R.drawable.background_edit_text_wrong)
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
    }

    /**
     * Treats with the editTexts in case the user input some text in them
     */
    private fun editTextsChanges() {
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
    }

    /**
     * It updates the UI in the case the login is successful. Actually changes to the main activity
     * of the app
     * @param model is the data of the logged user that may be shown later
     */
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

    /**
     * In case the login is unsuccessful, it shows a error Toast to the user
     * @param errorString is the string that will be shown to the user
     */
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
