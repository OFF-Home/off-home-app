package com.offhome.app.data

import com.offhome.app.data.model.SignedUpUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
//plantilla que venia feta
class SignUpDataSource {

    fun login(username: String, password: String): Result<SignedUpUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = SignedUpUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}