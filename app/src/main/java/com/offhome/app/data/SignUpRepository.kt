package com.offhome.app.data

import com.offhome.app.data.model.SignedUpUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
//plantilla que venia feta
//editant

class SignUpRepository(val dataSource: SignUpDataSource) {

    // in-memory cache of the loggedInUser object
    var user: SignedUpUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    /*fun logout() {
        user = null
        dataSource.logout()
    }*/

    fun signUp(email: String, username: String, password: String, birthDate: String): Result<SignedUpUser> {
        // handle login
        val result = dataSource.signUp(email, username, password, birthDate)

        /*if (result is Result.Success) {
            //setLoggedInUser(result.data)  //no cal crec
        }*/

        return result
    }

    private fun setLoggedInUser(signedUpUser: SignedUpUser) {
        this.user = signedUpUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}