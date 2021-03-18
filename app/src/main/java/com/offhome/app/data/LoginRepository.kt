package com.offhome.app.data

import androidx.lifecycle.LiveData
import com.offhome.app.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {


    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LiveData<LoggedInUser>> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data.value)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser?) {
        this.user = loggedInUser
    }
}
