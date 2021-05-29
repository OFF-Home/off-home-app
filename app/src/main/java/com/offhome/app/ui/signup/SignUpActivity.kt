package com.offhome.app.ui.signup



import android.app.Activity
import android.app.DatePickerDialog
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
import com.offhome.app.R
import com.offhome.app.ui.login.LoginActivity
import java.util.*

/**
 * Class *SignUpActivity*
 *
 * Activity for the signUp screen. This class is the View in this screen's MVVM
 * @author Ferran and Pau
 * @property signUpViewModel references this activity's ViewModel
 * @property email references the e-mail EditText
 * @property username references the username EditText
 * @property password references the password EditText
 * @property birthDate references the birth date EditText. This field will be filled using a DatePickerDialog
 * @property signUp references the sign-up Button
 * @property hereButton references the Button used to swap to the log-in screen
 * @property googleButton references the Button used to sign-up with google
 * @property loading references the ProgressBar shown while the sign-up is being processed
 * @property activity references this activity. It is used to pass the context to lower layers
 *
 * */
class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var birthDate: EditText
    private lateinit var signUp: Button
    private lateinit var hereButton: TextView
    private lateinit var googleButton: Button
    private lateinit var showPasswordButton: ImageView
    private lateinit var loading: ProgressBar
    private val activity: SignUpActivity = this

    /**
     * Override the onCreate method
     *
     * Initializes the attributes
     * sets listeners to the form state and the text fields to check the correctness of the input data.
     * sets listeners to the birthDate editText and the buttons.
     * sets a listener to the result of the signUp call.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        signUpViewModel = ViewModelProvider(this, SignUpViewModelFactory())
            .get(SignUpViewModel::class.java)
        email = findViewById(R.id.editTextEmail)
        username = findViewById(R.id.editTextUsername)
        password = findViewById(R.id.editTextPassword)
        birthDate = findViewById(R.id.editTextBirthDate)
        signUp = findViewById(R.id.ButtonSignUp)
        hereButton = findViewById(R.id.textViewHere)
        googleButton = findViewById(R.id.buttonGoogleSignUp)
        showPasswordButton = findViewById(R.id.ImageViewShowPasswordSignUp)
        loading = findViewById(R.id.loading)

        // observar l'estat del form, és a dir, si hi ha errors. Si n'hi ha, posar els errors en els EditText's
        signUpViewModel.signUpFormState.observe(
            this@SignUpActivity,
            Observer {
                val signUpStateVM = it ?: return@Observer

                // disable login button unless all fields are valid
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
                } else {
                    birthDate.error = null // funciona xd
                }
            }
        )

        // observar el resultat de signUp.
        // Si hi ha error, mostrar-lo.
        // Si hi ha success, s'informa d'aixoamb un missatge, i canvia a pantalla de LogIn
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
            signUpViewModel.signupDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                birthDate.text.toString()
            )
        }

        username.afterTextChanged {
            signUpViewModel.signupDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                birthDate.text.toString()
            )
        }

        birthDate.afterTextChanged {
            signUpViewModel.signupDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                birthDate.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signUpViewModel.signupDataChanged(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString(),
                    birthDate.text.toString()
                )
            }

            // -
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

            // listener del botó signUp
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
                { _, selectedYear, selectedMonth, selectedDay ->
                    val humanMonth = selectedMonth + 1 // perque els mesos comencen a 0
                    val textDate = "$selectedDay/$humanMonth/$selectedYear"
                    birthDate.setText(textDate)
                    signUpViewModel.signupDataChanged(
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

        // yoink
        showPasswordButton.setOnClickListener {
            password.inputType = if (password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            password.setSelection(password.text.length)
        }
    }

    /**
     * Shows a success message on screen and moves on to the log-in screen
     */
    private fun showSuccessAndProceed() {
        val emailConfirmationMessage = getString(R.string.emailConfirmationMessage)

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

    /**
     * Shows the error message resulting of an error with the sign-up process
     */
    private fun showSignUpFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    /**
     * Changes to the log-in screen
     */
    private fun canviALogInActivity() {
        val intentCanviALogIn = Intent(activity, LoginActivity::class.java) // .apply {        }
        startActivity(intentCanviALogIn)

        // aqui s'hauria de fer un finish() i potser un setResult(), crec
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
