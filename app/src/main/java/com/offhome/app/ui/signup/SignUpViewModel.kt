package com.offhome.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.offhome.app.data.SignUpRepository
import com.offhome.app.data.Result

import com.offhome.app.R

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    fun signUp(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = signUpRepository.login(username, password)

        if (result is Result.Success) {
            _signUpResult.value =
                SignUpResult(success = SignedUpUserView(displayName = result.data.displayName))
        } else {
            _signUpResult.value = SignUpResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _signUpForm.value = SignUpFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    //en signUp això serà només per coses tipo numero minim de char's.
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}