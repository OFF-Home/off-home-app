package com.offhome.app.data.retrofit

import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.TagData
import com.offhome.app.data.profilejson.UserDescription
import com.offhome.app.model.profile.UserInfo
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import com.offhome.app.data.model.FollowingUser
import com.offhome.app.data.profilejson.NomTag
import com.offhome.app.data.profilejson.UserUsername

interface UserService {

    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("/upload/userimage/{email}")
    fun updloadProfilePhoto(@Part(value = "file\"; filename=\"photo.jpeg\" ") file: RequestBody?): Call<RequestBody?>?

    @GET("/users/:username/getFollow")
    fun following(@Path("username") currentUser: String): Call<List<FollowingUser>>

    @POST("/users/:username/follow")
    fun follow(@Path("username") currentUser: String, @Body email: String): Call<ResponseBody>

    @POST("/users/:username/unfollow")
    fun stopFollowing(@Path("username") currentUser: String, @Body email: String): Call<ResponseBody>

    //in progress
    //retornava nomes un. canviaran quna puguin a set
    @GET("/tags/{username}/show")
    fun getTags(@Path("username") email: String): Call< List<TagData> >

    @GET("/activitats/{email}")
    fun getUserActivities(@Path("email") email: String): Call<List<ActivityFromList>>

    //al backend encara no està implementat?
    //si pero substitueix enlloc de afegir, perque nomes n'hi ha un. o no, nose
    @POST("tags/{username}/insert")
    fun addTag(@Path("username") email: String, @Body nomTag: NomTag): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/tags/{username}/delete")
    fun deleteTag(@Path("username") email: String, @Body nomTag: NomTag): Call<ResponseBody>

    //estem a la espera
    @Headers("Content-Type: application/json")
    @POST("users/{username}/update")
    fun setUsername(@Path("username") email:String, @Body username: UserUsername): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("users/{username}/update")
    fun setDescription(@Path("username") email: String, @Body description: UserDescription): Call<ResponseBody>

    @GET("/users/{username}")
    fun getProfileInfoByUsername(@Path("username") newText: String): Call<UserInfo>
}
