package com.offhome.app.data.retrofit

import com.offhome.app.model.profile.UserInfo
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    @GET("/users/:username/getFollow")
    fun following(@Path("username") currentUser: String): Call<List<String>>

    @POST("/users/:username/follow")
    fun follow(@Path("username") currentUser: String, @Body email: String): Call<String>

    @POST("/users/:username/unfollow")
    fun stopFollowing(@Path("username") currentUser: String, @Body email: String): Call<String>
}
