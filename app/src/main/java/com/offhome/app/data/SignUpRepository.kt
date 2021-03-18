package com.offhome.app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.offhome.app.R
import com.offhome.app.data.model.SignedUpUser
import com.offhome.app.ui.signup.SignUpActivity
import com.offhome.app.ui.signup.SignUpResult

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
// plantilla que venia feta
// editant

class SignUpRepository(val dataSource: SignUpDataSource) {

    // in-memory cache of the loggedInUser object
    /*var user: SignedUpUser? = null
        private set*/

    /*val isLoggedIn: Boolean
        get() = user != null*/

    private val _signUpResult = MutableLiveData<SignUpResult>() // SignUpResult serà d'un package diferent... mal disseny? fer capa transversal?
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        // user = null
    }

    fun signUp(email: String, username: String, password: String, birthDate: String, activity: SignUpActivity) {
        // handle login
        dataSource.result.observe(
            activity,
            Observer {
                val result = it ?: return@Observer
                if (result.error != null) {

                    val msg: String = result.error.toString() // a ver si funciona
                    println("msg = $msg")
                    val msg2: CharSequence = msg.subSequence(16, msg.length - 1)
                    println("msg2 = $msg2")

                    when { // TODO posar els strings de la excepcio als .equals()
                        msg2.equals("cosa1") -> _signUpResult.value = SignUpResult(error = R.string.username_taken)
                        msg2.equals("cosa2") -> _signUpResult.value = SignUpResult(error = R.string.email_taken)
                        msg2.equals("cosa3") -> _signUpResult.value = SignUpResult(error = R.string.google_sign_up_error)
                        else -> _signUpResult.value = SignUpResult(error = R.string.unknown_sign_up_error)
                    }
                }
                if (result.success != null) {
                    _signUpResult.value = SignUpResult(success = result.success)
                }
                // aqui la activity fa mes coses q suposo q aqui no calen
            }
        )

        dataSource.signUp(email, username, password, birthDate, activity)
    }

    private fun setLoggedInUser(signedUpUser: SignedUpUser) {
        // this.user = signedUpUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
