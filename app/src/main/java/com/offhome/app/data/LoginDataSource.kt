package com.offhome.app.data

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.offhome.app.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(email: String, password: String): Result<LoggedInUser> {
        try {
            lateinit var user: LoggedInUser
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                    email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val fUser = FirebaseAuth.getInstance().currentUser
                        user = LoggedInUser(fUser.uid, fUser.displayName ?: "Pau")
                    } else {
                        Log.w("LOGIN", "signInWithEmail:failure", it.exception)
                    }
                }
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
        FirebaseAuth.getInstance().signOut()
    }
}