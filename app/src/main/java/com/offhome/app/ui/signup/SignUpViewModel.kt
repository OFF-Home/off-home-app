package com.offhome.app.ui.signup



import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.data.SignUpRepository
import java.util.*

/**
 * Class *SignUpViewModel*
 *
 * ViewModel for the signUp screen.
 * @author Ferran
 * @param signUpRepository is the reference to the repository object that plays the role of the Model in this screen's MVVM
 * @property _signUpForm private mutable live data of the form state. Indicates whether the input data fields are correct
 * @property signUpFormState public live data of the form state
 * @property _signUpResult private mutable live data of the result of the SignUp (success/error and further info)
 * @property signUpResult public live data for the result of the SignUp
 *
 * */
class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult // aquest és observat per SignUpActivity.

    /**
     * Initiates the Sign-up process
     *
     * makes the call to the Repository and observes its live data for the result.
     * Sets the ViewModel's live data according to that of the Repository when it is ready
     * @param email user's email
     * @param username user's username
     * @param password
     * @param activity pointer to the activity, used by the observers
     */
    fun signUp(email: String, username: String, password: String?, activity: AppCompatActivity) {
        // can be launched in a separate asynchronous job
        signUpRepository.result.observe(
            activity,
            Observer {
                val resultRepo = it ?: return@Observer
                if (resultRepo.error != null) {
                    val msg: String = resultRepo.error.toString()
                    // println("msg = $msg")

                    when { // quan el backend implementi noves excepcions, haurem d'afegir entrades aqui
                        msg == "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."
                        -> _signUpResult.value = SignUpResult(error = R.string.email_taken)
                        msg == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted."
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_bad_email_format)
                        msg == "com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred."
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_fb_connection_error)
                        msg == "cosa3"
                        -> _signUpResult.value = SignUpResult(error = R.string.google_sign_up_error)

                        (msg == "connection error. Server not reached" || msg == "java.lang.Exception: connection error. Server not reached") // crec q nomes serà el 2n.
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_connection_error)
                        (msg == "response received. Error in the server" || msg == "java.lang.Exception: response received. Error in the server")
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_server_error)

                        /*msg == "cosa1"
                        -> _signUpResult.value = SignUpResult(error = R.string.username_taken)*/

                        else
                        -> _signUpResult.value = SignUpResult(error = R.string.unknown_sign_up_error)
                    }
                }
                if (resultRepo.success != null) {
                    _signUpResult.value = SignUpResult(success = resultRepo.success)
                }
                // aqui la activity fa mes coses q suposo q aqui no calen

                signUpRepository.result.removeObservers(activity)
            }
        )

       // val fecha = birthDate?.let { getDateFromString(it) }

        signUpRepository.signUp(email, username, password, activity)
    }

    fun signUpBack(email: String, username: String, uid: String, token: String, activity: AppCompatActivity) {
        signUpRepository.result.observe(
            activity,
            Observer {
                val resultRepo = it ?: return@Observer
                if (resultRepo.error != null) {
                    val msg: String = resultRepo.error.toString()

                    when { // quan el backend implementi noves excepcions, haurem d'afegir entrades aqui
                        msg == "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."
                        -> _signUpResult.value = SignUpResult(error = R.string.email_taken)
                        msg == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted."
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_bad_email_format)
                        msg == "com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred."
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_fb_connection_error)
                        msg == "cosa3"
                        -> _signUpResult.value = SignUpResult(error = R.string.google_sign_up_error)

                        (msg == "connection error. Server not reached" || msg == "java.lang.Exception: connection error. Server not reached") // crec q nomes serà el 2n.
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_connection_error)
                        (msg == "response received. Error in the server" || msg == "java.lang.Exception: response received. Error in the server")
                        -> _signUpResult.value = SignUpResult(error = R.string.sign_up_server_error)

                        /*msg == "cosa1"
                        -> _signUpResult.value = SignUpResult(error = R.string.username_taken)*/

                        else
                        -> _signUpResult.value = SignUpResult(error = R.string.unknown_sign_up_error)
                    }
                }
                if (resultRepo.success != null) {
                    _signUpResult.value = SignUpResult(success = resultRepo.success)
                }
                // aqui la activity fa mes coses q suposo q aqui no calen

                signUpRepository.result.removeObservers(activity)
            }
        )
        signUpRepository.signUpBack(email, username, uid, token, activity)
    }

    /**
     * Updates the live data _signUpForm depending on whether the data in the parameters has the proper format
     *
     * @param email email field string
     * @param username username field string
     * @param password password field string
     */
    fun signupDataChanged(email: String, username: String, password: String) {
        if (!isEmailValid(email)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
        } else if (!isUserNameValid(username)) {
            _signUpForm.value = SignUpFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    /**
     * Returns true <=> the email string follows the expected e-mail format
     *
     * @param email email field string
     * @return Returns true <=> the input email string follows the expected e-mail format (something@something.something)
     */
    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() // de stackoverflow
    }

    /**
     * Returns true <=> the username string follows the expected format
     *
     * @param username username field string
     * @return Returns true <=> the username string follows the expected format (it's not empty)
     */
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    /**
     * Returns true <=> the password string follows the expected format
     *
     * It's just a placeholder for now
     * @param password password field string
     * @return Returns true <=> the password string follows the expected format (it is over 5 char's long)
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    /**
     * Returns true <=> the birthDate string is not empty
     *
     * @param birthDate birthDate field string
     * @return Returns true <=> the birthDate string is not empty (its format correctness is guaranteed by the UI)
     */
  /*  private fun isBirthDateValid(birthDate: String): Boolean {
        return birthDate.isNotBlank()
    }*/

    /**
     * Returns the Date object corresponding to the input string
     *
     * @param string1: date in string format
     * @return the input date string in a Date object
     */
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
