package com.offhome.app.data.retrofit

import com.offhome.app.model.profile.UserInfo
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface UserService {

    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("/upload/userimage/{email}")
    fun updloadProfilePhoto(@Part(value = "file\"; filename=\"photo.jpeg\" ") file: RequestBody?): Call<RequestBody?>?

}
