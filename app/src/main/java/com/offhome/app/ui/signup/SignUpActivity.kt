package com.offhome.app.ui.signup

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
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
import java.util.*

// la estem modificant
class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        val email = findViewById<EditText>(R.id.editTextEmail)
        val username = findViewById<EditText>(R.id.editTextUsername)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val birthDate = findViewById<EditText>(R.id.editTextBirthDate)
        val signUp = findViewById<Button>(R.id.ButtonSignUp)
        val hereButton = findViewById<TextView>(R.id.textViewHere)
        val googleButton = findViewById<Button>(R.id.buttonGoogleSignUp)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val activity: SignUpActivity = this

        signUpViewModel = ViewModelProvider(this, SignUpViewModelFactory())
            .get(SignUpViewModel::class.java)

        // observar l'estat del form, és a dir, si hi ha errors. Si n'hi ha, posar els errors en els EditText's
        signUpViewModel.signUpFormState.observe(
            this@SignUpActivity,
            Observer {
                val signUpStateVM = it ?: return@Observer

                // disable login button unless both username / password is valid
                signUp.isEnabled = signUpStateVM.isDataValid

                if (signUpStateVM.emailError != null) { // si hi ha error
                    email.error = getString(signUpStateVM.emailError)
                }
                if (signUpStateVM.usernameError != null) {
                    username.error = getString(signUpStateVM.usernameError)
                }
                if (signUpStateVM.passwordError != null) {
                    password.error = getString(signUpStateVM.passwordError)
                }
                if (signUpStateVM.birthDateError != null) {
                    birthDate.error = getString(signUpStateVM.birthDateError)
                }
            }
        )

        // observar el resultat de signUp.
        // Si hi ha error, mostrar-lo.
        // Si hi ha success, s'envia e-mail per a confirmar, s'informa d'axiò amb un missatge, i canvia a pantalla de LogIn
        //
        signUpViewModel.signUpResult.observe(
            this@SignUpActivity,
            Observer {
                val signUpResultVM = it ?: return@Observer

                loading.visibility = View.GONE
                if (signUpResultVM.error != null) {
                    showSignUpFailed(signUpResultVM.error)
                }
                if (signUpResultVM.success != null) {
                    showSuccessAndProceed()
                }
                setResult(Activity.RESULT_OK)

                // Complete and destroy login activity once successful
                // finish()  //treure oi?
            }
        )

        // s'executen quan es modifiquen email, username o password
        // fan les comprovacions de si els strings son correctes

        email.afterTextChanged {
            signUpViewModel.loginDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                birthDate.text.toString()
            )
        }

        username.afterTextChanged {
            signUpViewModel.loginDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                birthDate.text.toString()
            )
        }

        birthDate.afterTextChanged {
            signUpViewModel.loginDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                birthDate.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signUpViewModel.loginDataChanged(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString(),
                    birthDate.text.toString()
                )
            }

            // que es esto??
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signUpViewModel.signUp(
                            email.text.toString(),
                            username.text.toString(),
                            password.text.toString(),
                            birthDate.text.toString(),
                            activity
                        )
                }
                false
            }

            // listener del botó signUp, suposo
            // crida a signUp
            signUp.setOnClickListener {
                loading.visibility = View.VISIBLE

                signUpViewModel.signUp(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString(),
                    birthDate.text.toString(),
                    activity
                )
            }
        }

        // mostrar el fragment que permet escollir la birth date.
        birthDate.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val day: Int = cal.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    val humanMonth = month + 1 // perque els mesos comencen a 0
                    val textDate = "$dayOfMonth/$humanMonth/$year"
                    birthDate.setText(textDate)
                    signUpViewModel.loginDataChanged(
                        email.text.toString(),
                        username.text.toString(),
                        password.text.toString(),
                        birthDate.text.toString()
                    )
                },
                year, month, day
            )
            datePicker.show()
        }

        hereButton.setOnClickListener {
            canviALogInActivity()
        }

        // Firebase
        // firebaseAuth = Firebase.auth

        // diu que faci aixo
        /*val currentUserFB = firebaseAuth.currentUser
        if (currentUserFB != null)
            reload();*/
    }

    private fun showSuccessAndProceed() {
        val emailConfirmationMessage = getString(R.string.emailConfirmationMessage)
        // initiate signUp experience
        // s'envia e-mail per a confirmar,
        // s'informa d'axiò amb un missatge,
        // i canvia a pantalla de LogIn

        // ensenyar missatge de welcome a baix
        Toast.makeText(
            applicationContext,
            // "$emailConfirmationMessage $displayName",
            emailConfirmationMessage,
            Toast.LENGTH_LONG
        ).show()

        // canviar a pantalla de LogIn.
        canviALogInActivity()
    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun canviALogInActivity() {
        // TODO per ara, com a placeholder, va a MainActivity (la de les activitats (categories))
        val intentCanviALogIn = Intent(this, MainActivity::class.java) // .apply {        }
        startActivity(intentCanviALogIn)
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
