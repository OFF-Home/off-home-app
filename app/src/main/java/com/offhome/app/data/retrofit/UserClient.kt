package com.offhome.app.data.retrofit

import com.offhome.app.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * User Client Class
 * It has the base configuration of retrofit for all the user-related backend communication
 * @property instance has the instance of this client
 * @property userService is the instance of the service that connects with backend
 * @property retrofit is the instance of retrofit library to reach backend
 */
class UserClient {
    private lateinit var instance: UserClient
    private var userService: UserService? = null
    private var retrofit: Retrofit? = null

    /**
     * Initializes the retrofit object with baseURL or the GSon converter. Creates the userService object
     */
    init {
        retrofit = Retrofit.Builder()
            .baseUrl(Constants().BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        userService = retrofit!!.create(UserService::class.java)
    }

    /**
     * getter of the instance of this client
     *
     * @return the instance of this client
     */
    fun getInstance(): UserClient? {
        return instance
    }

    /**
     * getter of the service
     *
     * @return the service
     */
    fun getUserService(): UserService? {
        return userService
    }
}
