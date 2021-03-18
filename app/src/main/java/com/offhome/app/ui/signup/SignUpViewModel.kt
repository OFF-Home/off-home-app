package com.offhome.app.ui.signup

// import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.data.Result
import com.offhome.app.data.SignUpRepository

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult // aquest és observat per SignUpActivity.    //pero canvia en algun moment?

    fun signUp(email: String, username: String, password: String, birthDate: String) {
        // can be launched in a separate asynchronous job
        val result = signUpRepository.signUp(email, username, password, birthDate)

        if (result is Result.Success) {
            //_signUpResult.value = SignUpResult(success = SignedUpUserView(displayName = result.data.displayName))
            _signUpResult.value = SignUpResult(success = true)
        } else {
            //tot aixo es useless?
            val msg : String = result.toString()
            println("msg = $msg")
            val msg2 : CharSequence = msg.subSequence(16, msg.length - 1)
            println("msg2 = $msg2")

            when {
                msg2.equals("cosa1")     //canviar per el string que explica l'error        //es un int, el ID   //TODO posar els strings de la excepcio
                -> _signUpResult.value = SignUpResult(error = R.string.username_taken)
                msg2.equals("cosa2") -> _signUpResult.value = SignUpResult(error = R.string.email_taken)
                msg2.equals("cosa3") -> _signUpResult.value = SignUpResult(error = R.string.google_sign_up_error)
                else -> _signUpResult.value = SignUpResult(error = R.string.unknown_sign_up_error)
            }

            //_signUpResult.value = SignUpResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(email: String, username: String, password: String) {
        if (!isEmailValid(email)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
        } else if (!isUserNameValid(username)) {
            _signUpForm.value = SignUpFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() // de stackoverflow
    }

    private fun isUserNameValid(username: String): Boolean {
        /*return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }*/

        return username.isNotBlank()
    }

    // A placeholder password validation check
    // en signUp això serà només per coses tipo numero minim de char's.
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
