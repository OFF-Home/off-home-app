package com.offhome.app.data.retrofit

import com.offhome.app.model.profile.UserInfo
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    @GET("/")
    fun isFollowing(currentUser: String, email: String): Call<Boolean>

    @POST("")
    fun follow(currentUser: String, email: String): Call<String>

    @DELETE("")
    fun stopFollowing(currentUser: String, email: String): Call<String>
}
