package com.offhome.app.data

import androidx.lifecycle.LiveData
import com.offhome.app.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 * @author Pau Cuesta Arcos
 * @param dataSource references the Data Source from where to get de data
 * @property user is the user that has logged in
 * @property isLoggedIn has constancy if there is a logged in user or not
 */

class LoginRepository(val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    /**
     * Calls to the data source to log out and deletes the info of the user
     */
    fun logout() {
        user = null
        dataSource.logout()
    }

    /**
     * Calls to the data source to log in and if successful, saves de result into de loggedInUser
     * @param email is the email of the user
     * @param password is the password of the user
     * @return the result with a live data of the log in
     */
    fun login(email: String, password: String): Result<LiveData<LoggedInUser>> {
        val result = dataSource.login(email, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data.value)
        }

        return result
    }

    /**
     * Sets the logged in user
     */
    private fun setLoggedInUser(loggedInUser: LoggedInUser?) {
        this.user = loggedInUser
    }

    fun recoverPassword(email: String): LiveData<String> {
        return dataSource.recoverPassword(email)
    }
}
