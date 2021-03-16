package com.offhome.app.data

import com.offhome.app.data.model.SignedUpUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
//plantilla que venia feta
class SignUpDataSource {

    fun signUp(email: String, username: String, password: String, birthDate: String): Result<SignedUpUser> {
        try {
            // TODO: handle loggedInUser authentication
            // fer crida HTTP
            // aixi que el backend donarà excepció si ja existeix un usuari amb aquest email o username?

            val fakeUser = SignedUpUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)     //crec que no cal el paràmetre.
            //testejant:
            //return Result.Error(IOException("Error signing up. A user with this e-mail already exists"))

        } catch (e: Throwable) {
            return Result.Error(IOException("Error signing up", e))     //TODO: afegir al string el motiu. hauria de ser que ja existeix un usuari amb aquest email o username
        }
    }

    /*fun logout() {
        // revoke authentication
    }*/
}
