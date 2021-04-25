package com.offhome.app.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.LoginRepository
import com.offhome.app.data.Result

/**
 * View Model for Login activity
 * @author Pau Cuesta Arcos
 * @param loginRepository references the Repository for the Login
 * @property _loginForm is the private mutable live data of the form state
 * @property loginFormState is the public live data of the form state
 * @property _loginResult is the private mutable live data for the result of the login
 * @property loginResult is the public live data for the result of the login
 * */
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    lateinit var recoverResult: LiveData<String>

    /**
     * It calls te repository to login and treats the result to set the mutable live data of the result of the login
     */
    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(email, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(data = result.data))
            SharedPreferenceManager.setStringValue(Constants().PREF_EMAIL, email)
            SharedPreferenceManager.setStringValue(Constants().PREF_PROVIDER, "password")
        } else {
            if (result is Result.Error) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    /**
     * It calls the repository to send the email to reset password
     */
    fun recoverPassword(email: String) {
        recoverResult = loginRepository.recoverPassword(email)
    }

    /**
     * It validates the state of the data when this is modified
     */
    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    /**
     * It checks if the email introduced by the user is valid, that is, if it fulfill the patters of a email
     */
    private fun isEmailValid(email: String): Boolean {
        return email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * It checks if the password introduced by the user is valid. Specifically checks if the sufficiently large
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
