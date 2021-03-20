package com.offhome.app.ui.signup

// import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.data.SignUpRepository
import java.util.*

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult // aquest és observat per SignUpActivity.    //pero canvia en algun moment?

    fun signUp(email: String, username: String, password: String, birthDate: String, activity: SignUpActivity) { // he fet la de passar la activity cap a baix pq mha semblat que els observers la volen. no se si funciona
        // can be launched in a separate asynchronous job

        signUpRepository.result.observe(
            activity,
            Observer {
                val resultRepo = it ?: return@Observer
                if (resultRepo.error != null) {
                    val msg: String = resultRepo.error.toString() //funciona
                    println("msg = $msg")
                    Toast.makeText(activity, "msg = $msg", Toast.LENGTH_LONG).show()
                    /*val msg2: CharSequence = msg.subSequence(16, msg.length - 1)
                    println("msg2 = $msg2")
                    Toast.makeText(activity, "msg2 = $msg2", Toast.LENGTH_LONG).show()*/

                    when { // TODO posar els strings de la excepcio als .equals()
                        msg.equals("cosa1")
                            -> _signUpResult.value = SignUpResult(error = R.string.username_taken)
                        msg.equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")
                            -> _signUpResult.value = SignUpResult(error = R.string.email_taken)
                        msg.equals("cosa3")
                            -> _signUpResult.value = SignUpResult(error = R.string.google_sign_up_error)
                        else
                            -> _signUpResult.value = SignUpResult(error = R.string.unknown_sign_up_error)
                    }
                }
                if (resultRepo.success != null) {
                    _signUpResult.value = SignUpResult(success = resultRepo.success)
                }
                // aqui la activity fa mes coses q suposo q aqui no calen
            }
        )

        val fecha = getDateFromString(birthDate)

        signUpRepository.signUp(email, username, password, fecha, activity)
    }

    fun loginDataChanged(email: String, username: String, password: String, birthDate: String) {
        if (!isEmailValid(email)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
        } else if (!isUserNameValid(username)) {
            _signUpForm.value = SignUpFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else if (!isBirthDateValid(birthDate)) {
            _signUpForm.value = SignUpFormState(birthDateError = R.string.invalid_birth_date)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() // de stackoverflow
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    // A placeholder password validation check
    // això serà només per coses tipo numero minim de char's.
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    // hauria de ser impossible un error restringint els possibles valors d'entrada amb la UI
    private fun isBirthDateValid(birthDate: String): Boolean {    //nose si funciona
        /*var i: Int = 0
        var nSlashes: Int = 0
        while (i < birthDate.length) {
            //val num:Int = birthDate[i] as Int
            //if (num !in 47..57)
                // he intentat ferho bonic pero tot son objectes o algo
            if (birthDate[i] != '/' &&
                    birthDate[i] != '0' &&
                    birthDate[i] != '1' &&
                    birthDate[i] != '2' &&
                    birthDate[i] != '3' &&
                    birthDate[i] != '4' &&
                    birthDate[i] != '5' &&
                    birthDate[i] != '6' &&
                    birthDate[i] != '7' &&
                    birthDate[i] != '8' &&
                    birthDate[i] != '9')
                        return false

            if (birthDate[i] == '/')
                ++nSlashes
            ++i
        }

        return (nSlashes == 2)*/

        return birthDate.isNotBlank()
    }

    private fun getDateFromString(string1: String): Date {
        var i: Int = 0
        while (string1[i] != '/') {
            ++i
        }
        var stringPart = string1.subSequence(0, i) as String
        val dayOfMonth: Int = stringPart.toInt()

        var j: Int = i + 1
        while (string1[j] != '/') {
            ++j
        }
        stringPart = string1.subSequence(i + 1, j) as String
        val month: Int = stringPart.toInt()

        stringPart = string1.subSequence(j + 1, string1.length) as String
        val year: Int = stringPart.toInt()

        return Date(year, month, dayOfMonth) // asumint que "date" (el nom del 3r parametre) vol dir dayOfMonth
    }
}
