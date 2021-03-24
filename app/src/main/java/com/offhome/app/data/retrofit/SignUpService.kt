package com.offhome.app.data.retrofit

import com.offhome.app.data.model.SignUpUserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface *SignUpService*
 *
 * Manages the HTTP calls to the back-end
 * @author Pau and Ferran
 */
interface SignUpService {

    /**
     * creates a profile in the back-end database
     *
     * @param username user's username. Will be passed through the path, as it is the identifier of the Profile we want to create
     * @param signUpUserData : the user's data; it includes email, username, password, birthDate. It is passed in the body.
     * @return returns the call to be executed
     */
    @POST("/users/{username}/create")
    fun createProfile(@Path("username") username: String, @Body signUpUserData: SignUpUserData): Call<String>
}
