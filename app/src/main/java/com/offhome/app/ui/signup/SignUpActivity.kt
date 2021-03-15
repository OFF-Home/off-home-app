package com.offhome.app.ui.signup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.offhome.app.MainActivity

import com.offhome.app.R

//la estem modificant
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

        signUpViewModel = ViewModelProvider(this, SignUpViewModelFactory())
            .get(SignUpViewModel::class.java)

        //observar l'estat del form, és a dir, si hi ha errors. Si n'hi ha, posar els errors en els EditText's
        signUpViewModel.signUpFormState.observe(this@SignUpActivity, Observer {
            val signUpState = it ?: return@Observer

            // disable login button unless both username / password is valid
            signUp.isEnabled = signUpState.isDataValid

            if (signUpState.emailError != null) { //si hi ha error
                email.error = getString(signUpState.emailError)
            }
            if (signUpState.usernameError != null) {
                username.error = getString(signUpState.usernameError)
            }
            if (signUpState.passwordError != null) {
                password.error = getString(signUpState.passwordError)
            }
        })

        //observar el resultat de signUp.
        //Si hi ha error, mostrar-lo.
        //Si hi ha success, s'envia e-mail per a confirmar, s'informa d'axiò amb un missatge, i canvia a pantalla de LogIn
        //
        signUpViewModel.signUpResult.observe(this@SignUpActivity, Observer {
            val signUpResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (signUpResult.error != null) {
                showSignUpFailed(signUpResult.error)
            }
            if (signUpResult.success != null) {
                updateUiWithUser(signUpResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        //s'executen quan es modifiquen email, username o password
        //fan les comprovacions de si els strings son correctes

        email.afterTextChanged {
            signUpViewModel.loginDataChanged(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString()
            )
        }

        username.afterTextChanged {
            signUpViewModel.loginDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signUpViewModel.loginDataChanged(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString()
                )
            }

            //que es esto??
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signUpViewModel.signUp(email.text.toString(), username.text.toString(), password.text.toString(), birthDate.text.toString())
                }
                false
            }

            //listener del botó signUp, suposo
            //crida a signUp
            signUp.setOnClickListener {
                loading.visibility = View.VISIBLE
                signUpViewModel.signUp(email.text.toString(), username.text.toString(), password.text.toString(), birthDate.text.toString())
            }
        }

        /*birthDate.setOnClickListener {
            //ho estic fent amb onClick
        }*/
    }

    private fun updateUiWithUser(model: SignedUpUserView) {
        val emailConfirmationMessage = getString(R.string.emailConfirmationMessage)
        //val displayName = model.displayName
        //initiate successful logged in experience
        // TODO s'envia e-mail per a confirmar,
        // s'informa d'axiò amb un missatge,
        // i canvia a pantalla de LogIn

        //ensenya un missatge de welcome a baix
        Toast.makeText(
                applicationContext,
                //"$emailConfirmationMessage $displayName",
                emailConfirmationMessage,
                Toast.LENGTH_LONG
        ).show()

        //canviar a pantalla de LogIn.
        canviALogInActivity()
    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    //mostrar el fragment que permet escollir la birth date. el crida el editTextBirthDate onClick.
    public fun showDatePicker(elTextCrec : View) {

        //el que se suposa que hauriem de fer: crear-ne un de nou. pero requereix API superior
        /*val datePickerFragment1 = DatePickerFragment()
        datePickerFragment1.show(supportFragmentManager, "datePicker")*/

        //la meva chapuza terrible: n'he creat un invisible que mostro quan cal.
        /*val datePickerBirthDateExistent = findViewById<DatePicker>(R.id.datePickerBirthDateExistent)
        datePickerBirthDateExistent.visibility = View.VISIBLE;
        datePickerBirthDateExistent.bringToFront()*/
    }

    public fun textViewHereCanviarALogIn(elTextCrec : View) {
        canviALogInActivity()
    }

    private fun canviALogInActivity() {
        // TODO per ara, com a placeholder, va a MainActivity (la de les activitats (esdeveniments, categories))
        val intentCanviALogIn = Intent(this, MainActivity::class.java)      //.apply {        }
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