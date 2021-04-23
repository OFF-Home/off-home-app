package com.offhome.app.data.retrofit

import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.TagData
import com.offhome.app.model.profile.UserInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    //in progress
    @GET("/tags/{username}/show")
    fun getTags(@Path("username") username: String): Call<List<TagData>>

    @GET("/activitats/{email}")
    fun getUserActivities(@Path("email") email: String): Call<List<ActivityFromList>>

    //al backend encara no està implementat?
    @POST("tags/{username}/insert")
    fun addTag(@Path("username") email: String, @Body nomTag:String): Call<ResponseBody> //mai dona successful i fent get mai surten.

    //aquests encara no sé què he d'enviar (falta postman)
    @POST("users/{username}/update")
    fun setUsername(@Path("username") email:String, @Body username: String): Call<ResponseBody> //email (aka username, lol) identifica a l'user

    @POST("users/{username}/update")
    fun setDescription(@Path("username") email: String, @Body description:String): Call<ResponseBody>

    @GET("/users/{username}")
    fun getProfileInfoByUsername(@Path("username") newText: String): Call<UserInfo>
}
