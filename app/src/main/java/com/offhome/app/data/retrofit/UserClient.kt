package com.offhome.app.data.retrofit

import com.offhome.app.data.ActivitiesClient
import com.offhome.app.data.ActivitiesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserClient {
    private lateinit var instance: UserClient
    private var userService: UserService? = null
    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        userService = retrofit!!.create(UserService::class.java)
    }

    fun getInstance(): UserClient? {
        return instance
    }

    fun getUserService(): UserService? {
        return userService
    }
}