package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.R
import com.offhome.app.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * @author Pau Cuesta Arcos
 */
class LoginDataSource {

    private val _loggedInUser = MutableLiveData<LoggedInUser>()
    private val loggedInUser: LiveData<LoggedInUser> = _loggedInUser

    private val _recoverPassword = MutableLiveData<String>()
    private val recoverPassword: LiveData<String> = _recoverPassword

    /**
     * It makes the call to Firebase to do the login with email and passwword
     * @return the result, if the connection was successful or not
     */
    fun login(email: String, password: String): Result<LiveData<LoggedInUser>> {
        try {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                    email, password
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val fUser = FirebaseAuth.getInstance().currentUser
                        if (!fUser!!.isEmailVerified) {
                            _loggedInUser.value = LoggedInUser(
                                null,
                                null,
                                R.string.login_failed_email
                            )
                        } else _loggedInUser.value = LoggedInUser(fUser.uid, fUser.email, null)
                    } else {
                        _loggedInUser.value = LoggedInUser(null, null, R.string.login_failed_login)
                    }
                }
            return Result.Success(loggedInUser)
        } catch (e: Throwable) {
            Log.w("LOGIN", "signInWithEmail:failed", e)
            return Result.Error(IOException("Error logging in", e))
        }
    }

    /**
     * It log outs from the instance of firebase authenticator
     */
    fun logout() {
        // TODO: revoke authentication
        FirebaseAuth.getInstance().signOut()
    }

    /**
     * It sends through Firebase the reset password email
     */
    fun recoverPassword(email: String): LiveData<String> {

        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    _recoverPassword.value = "OK"
                } else {
                    _recoverPassword.value = "ERROR"
                }
            }
        return recoverPassword
    }
}
