package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
// plantilla que venia feta
class SignUpDataSource {

    private lateinit var firebaseAuth: FirebaseAuth

    private var _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result // aquest es observat per Repository

    fun signUp(email: String, username: String, password: String, birthDate: Date) {
        try {
            firebaseAuth = Firebase.auth
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) { // Sign in success
                    Log.d("Sign-up", "createUserWithEmail:success")

                    val user = firebaseAuth.currentUser
                    user!!.sendEmailVerification().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Verification email", "Email sent.")
                        }
                    }

                    _result.value = Result(success = true) // Result.Success(true)
                } else {
                    Log.w("Sign-up", "createUserWithEmail:failure", task.exception)

                    _result.value = Result(error = task.exception) // aquesta excepcio funciona aixi?
                }
                // _result.value = Result(error = java.lang.Exception("Error[exception=AIXO Es UNA EXCEPCIO]\""))      //hardcode testing exceptions
            }

            // fer crida HTTP
            // aixi que el backend donarà excepció si ja existeix un usuari amb aquest email o username?
        } catch (e: Throwable) { // realment aqui no hauriem d'arribar si no hi ha cap problema amb la connexio a Firebase, dic jo.
            _result.value = Result(error = e as Exception) // cast!
        }
    }
}
