package com.offhome.app.data.retrofit

import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.TagData
import com.offhome.app.model.profile.UserDescription
import com.offhome.app.model.profile.UserInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    //in progress
    //retornava nomes un. canviaran quna puguin a set
    @GET("/tags/{username}/show")
    fun getTags(@Path("username") email: String): Call< List<TagData> >

    @GET("/activitats/{email}")
    fun getUserActivities(@Path("email") email: String): Call<List<ActivityFromList>>

    //al backend encara no està implementat?
    //si pero substitueix enlloc de afegir, perque nomes n'hi ha un.
    @POST("tags/{username}/insert")
    fun addTag(@Path("username") email: String, @Body nomTag:String): Call<ResponseBody>

    //estem a la espera
    @Headers("Content-Type: application/json")
    @POST("users/{username}/update")
    fun setUsername(@Path("username") email:String, @Body username: String): Call<ResponseBody>

    //Aquest tampoc va (problema fronted)
    @Headers("Content-Type: application/json")
    @POST("users/{username}/update")
    fun setDescription(@Path("username") email: String, @Body description: UserDescription): Call<ResponseBody>

    @GET("/users/{username}")
    fun getProfileInfoByUsername(@Path("username") newText: String): Call<UserInfo>
}
