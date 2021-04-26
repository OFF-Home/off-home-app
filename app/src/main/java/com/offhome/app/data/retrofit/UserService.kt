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

/**
 * Interface *UserService*
 *
 * Manages the HTTP calls to the back-end
 * @author Pau, Ferran, others
 */
interface UserService {

    /**
     * gets the info of a profile from the back-end database
     *
     * @param username user's email. Will be passed through the path, as it is the identifier of the user  //todo serà cert quan arreglem la PK de backend
     * @return returns the call to be executed. the response in it will contain the user info
     */
    @GET("/users/{username}/show")
    fun getProfileInfo(@Path("username") username: String): Call<UserInfo>

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("/upload/userimage/{email}")
    fun uploadProfilePhoto(@Path("email") currentUser: String, @Part(value = "file\"; filename=\"photo.jpeg\" ") file: RequestBody) : Call<ResponseBody>

    @GET("/users/{username}/getFollow")
    fun following(@Path("username") currentUser: String): Call<List<FollowingUser>>

    @POST("/users/{username}/follow")
    fun follow(@Path("username") currentUser: String, @Body email: String): Call<ResponseBody>

    @DELETE("/users/{username}/unfollow")
    fun stopFollowing(@Path("username") currentUser: String, @Body email: String): Call<ResponseBody>

    //in progress
    //retornava nomes un. canviaran quna puguin a set
    /**
     * gets the tags of a profile from the back-end database
     *
     * @param email user's email. Will be passed through the path, as it is the identifier of the user
     * @return returns the call to be executed. the response in it will contain the list of tags
     */
    @GET("/tags/{username}/show")
    fun getTags(@Path("username") email: String): Call< List<TagData> >

    /**
     * gets the activities where a certain user is joined from the back-end database
     *
     * @param email user's email. Will be passed through the path, as it is the identifier of the user
     * @return returns the call to be executed. the response in it will contain the list of activities
     */
    @GET("/activitats/{email}")
    fun getUserActivities(@Path("email") email: String): Call<List<ActivityFromList>>

    //al backend encara no està implementat?
    //si pero substitueix enlloc de afegir, perque nomes n'hi ha un. o no, nose
    /**
     * adds a tag to a user in the back-end database
     *
     * @param email user's email. Will be passed through the path, as it is the identifier of the user
     * @param nomTag tag to be added
     * @return returns the call to be executed.
     */
    @POST("tags/{username}/insert")
    fun addTag(@Path("username") email: String, @Body nomTag: NomTag): Call<ResponseBody>

    /**
     * deletes a tag from a user in the back-end database
     *
     * @param email user's email. Will be passed through the path, as it is the identifier of the user
     * @param nomTag tag to be deleted
     * @return returns the call to be executed.
     */
    @Headers("Content-Type: application/json")
    @POST("/tags/{username}/delete")
    fun deleteTag(@Path("username") email: String, @Body nomTag: NomTag): Call<ResponseBody>

    /**
     * sets a user's username in the back-end database
     *
     * @param email user's email. Will be passed through the path, as it is the identifier of the user
     * @param username username to be set
     * @return returns the call to be executed.
     */
    //estem a la espera
    @Headers("Content-Type: application/json")
    @POST("users/{username}/update")
    fun setUsername(@Path("username") email:String, @Body username: UserUsername): Call<ResponseBody>

    /**
     * sets a user's description in the back-end database
     *
     * @param email user's email. Will be passed through the path, as it is the identifier of the user
     * @param description description to be set
     * @return returns the call to be executed.
     */
    @Headers("Content-Type: application/json")
    @POST("users/{username}/update")
    fun setDescription(@Path("username") email: String, @Body description: UserDescription): Call<ResponseBody>

    @GET("/users/{username}")
    fun getProfileInfoByUsername(@Path("username") newText: String): Call<UserInfo>
}
