package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.offhome.app.R
import com.offhome.app.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val _loggedInUser = MutableLiveData<LoggedInUser>()
    private val loggedInUser: LiveData<LoggedInUser> = _loggedInUser

    fun login(email: String, password: String): Result<LiveData<LoggedInUser>> {
        try {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                    email, password
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val fUser = FirebaseAuth.getInstance().currentUser
                        if (!fUser!!.isEmailVerified) {
                            _loggedInUser.value = LoggedInUser(null, null, R.string.login_failed_email)
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

    fun logout() {
        // TODO: revoke authentication
        FirebaseAuth.getInstance().signOut()
    }
}
