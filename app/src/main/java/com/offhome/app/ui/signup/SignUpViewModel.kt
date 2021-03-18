package com.offhome.app.ui.signup

// import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.data.SignUpRepository

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult // aquest és observat per SignUpActivity.    //pero canvia en algun moment?

    fun signUp(email: String, username: String, password: String, birthDate: String) {
        // can be launched in a separate asynchronous job

        signUpRepository.signUpResult.observe(
            this@SignUpViewModel,
            Observer {
                val signUpResult = it ?: return@Observer
                if (signUpResult.error != null) {
                    _signUpResult.value = SignUpResult(error = signUpResult.error)
                }
                if (signUpResult.success != null) {
                    _signUpResult.value = SignUpResult(success = signUpResult.success)
                }
                // aqui la activity fa mes coses q suposo q aqui no calen
            }
        )

        signUpRepository.signUp(email, username, password, birthDate)
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
