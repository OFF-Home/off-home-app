package com.offhome.app.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.data.model.SignUpUserData
import com.offhome.app.data.retrofit.SignUpService
import com.offhome.app.ui.signup.SignUpActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Class *SignUpDataSource*
 *
 * @author Ferran and Pau
 * @property _result private mutable live data of the result of the SignUp (success/error and further info)
 * @property result public live data for the result of the SignUp
 * @property firebaseAuth : firebase Authentication
 * @property retrofit: retrofit client to access the back-end
 * @property signUpService : implementation of the SignUpService Interface, used to access the back-end
 * */
class SignUpDataSource {

    private var _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result // aquest es observat per Repository

    private lateinit var firebaseAuth: FirebaseAuth
    private var retrofit: Retrofit =
        Retrofit.Builder().baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/").addConverterFactory(GsonConverterFactory.create()).build()
    private var signUpService: SignUpService = retrofit.create(SignUpService::class.java)

    /**
     * performs the Sign-Up on Firebase and the Back-end
     *
     * creates a user on firebase using the e-mail and password
     * sends a Firebase confirmation e-mail
     * creates a user on our database with all the data
     *
     * Observes the result of both user creations and sets the live data accordingly (success/error, and further info)
     * is supposed to put the appropriate error messages in the live data, but for now puts some error messages on screen
     *
     * @param email user's email
     * @param username user's username
     * @param password
     * @param birthDate user's birth date
     */
    fun signUp(email: String, username: String, password: String, birthDate: Date, activity: SignUpActivity) { // TODO treure activity quan acabi de debugejar

        try {
            firebaseAuth = Firebase.auth
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) { // Sign in success
                    Log.d("Sign-up", "createUserWithEmail:success")

                    val user = firebaseAuth.currentUser
                    user!!.sendEmailVerification().addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            Log.d("Verification email", "Email sent.")
                        }
                    }

                    _result.value = Result(success = true)
                    // parlar amb el nostre client
                    val signedUpUser = SignUpUserData(email, username, password, birthDate.toString())
                    val call: Call<String> = signUpService.createProfile(username, signedUpUser)
                    call.enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    activity,
                                    // "$emailConfirmationMessage $displayName",
                                    "HTTP call success",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    // "$emailConfirmationMessage $displayName",
                                    "HTTP call error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(
                                activity,
                                "Connection error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                } else {
                    Log.w("Sign-up", "createUserWithEmail:failure", task.exception)

                    _result.value = Result(error = task.exception) // aquesta excepcio funciona aixi?
                }
            }
        } catch (e: Throwable) {
            _result.value = Result(error = e as Exception) // cast!
        }
    }
}
