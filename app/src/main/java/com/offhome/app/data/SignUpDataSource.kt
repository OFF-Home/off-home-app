package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offhome.app.data.retrofit.SignUpService
import com.offhome.app.ui.signup.SignUpActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
// plantilla que venia feta
class SignUpDataSource {

    private lateinit var firebaseAuth: FirebaseAuth

    private var _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result // aquest es observat per Repository
    private lateinit var retrofit: Retrofit
    private lateinit var signUpService: SignUpService

    init {
        // per parlar amb el nostre server
        // aixo crea el Client
        retrofit = Retrofit.Builder().baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/users/").addConverterFactory(GsonConverterFactory.create()).build() // TODO la adreça potser no esta be
        // aixo retorna una implementacio de la interface
        signUpService = retrofit.create(SignUpService::class.java) // lo de ::class ho ha fet el Pau aixi
    }

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

                    _result.value = Result(success = true) // Result.Success(true)
/*
                    // parlar amb el nostre client
                    val call: Call<String> = signUpService.createProfile(username)
                    val response = call.execute() // we actually execute the call. I agafo el body perque potser conte la resposta xd
                    // response.body()
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

 */
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
