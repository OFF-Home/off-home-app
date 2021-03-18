package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.ui.signup.SignUpActivity

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
// plantilla que venia feta
class SignUpDataSource {

    private lateinit var firebaseAuth: FirebaseAuth

    /*private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult    //aquest es observat per Repository*/

    private var _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result // aquest es observat per Repository

    fun signUp(email: String, username: String, password: String, birthDate: String, activity: SignUpActivity) {
        try {

            firebaseAuth = Firebase.auth
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information, o no. // TODO: handle loggedInUser authentication
                    Log.d("Sign-up", "createUserWithEmail:success")
                    // firebaseAuth.sendSignInLinkToEmail()??
                    // _signUpResult.value = SignUpResult(success = true)
                    _result.value = Result(success = true) // Result.Success(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sign-up", "createUserWithEmail:failure", task.exception)
                    // Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()  //el toast el farem a Activity

                    _result.value = Result(error = task.exception) // aquesta excepcio funciona aixi?
                }
            }

            // fer crida HTTP
            // aixi que el backend donarà excepció si ja existeix un usuari amb aquest email o username?

            // testejant:
            // return Result.Error(IOException("Error signing up. A user with this e-mail already exists"))
        } catch (e: Throwable) { // realment aqui no hauriem d'arribar si no hi ha cap problema amb la connexio a Firebase, dic jo.
            _result.value = Result(error = e as Exception) // cast!
        }
    }
}
