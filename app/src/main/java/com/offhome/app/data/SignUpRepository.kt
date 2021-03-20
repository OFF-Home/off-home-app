package com.offhome.app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.offhome.app.data.model.SignedUpUser
import com.offhome.app.ui.signup.SignUpActivity
import java.util.*

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
// plantilla que venia feta
// editant

class SignUpRepository(val dataSource: SignUpDataSource) {

    private val _result = MutableLiveData<Result>() // SignUpResult serà d'un package diferent... mal disseny? fer capa transversal?
    val result: LiveData<Result> = _result

    fun signUp(email: String, username: String, password: String, birthDate: Date, activity: SignUpActivity) {
        dataSource.result.observe(
            activity,
            Observer {
                val resultDS = it ?: return@Observer
                // _result.value = resultDS  //aixo fa el mateix?

                if (resultDS.error != null) {
                    _result.value = Result(error = resultDS.error)
                }
                if (resultDS.success != null) {
                    _result.value = Result(success = resultDS.success)
                }
                // aqui la activity fa mes coses q suposo q aqui no calen
            }
        )
        dataSource.signUp(email, username, password, birthDate)
    }
}
