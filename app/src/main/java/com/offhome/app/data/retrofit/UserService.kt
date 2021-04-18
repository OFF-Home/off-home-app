package com.offhome.app.data.retrofit

import com.offhome.app.model.profile.UserInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    //in progress
    /*@POST() //tipo users/{username}/ ...
    fun setUsername(@Path("username") username: String): Call<ResponseBody> //però username segueix sent la PK de user a la BD??*/
}
