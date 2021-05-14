package com.offhome.app.data.retrofit



import com.offhome.app.model.Category
import retrofit2.Call
import retrofit2.http.*

interface CategoriesService {
    /**
     * This is the call for getting the categories
     */
    @GET("categories")
    fun getAllCategories(): Call<List<Category>>

    /*
    @POST("tweets/create")
    fun createTweet(@Body requestCreateTweet: RequestCreateTweet?): Call<Tweet?>?

    @POST("tweets/like/{idTweet}")
    fun likeTweet(@Path("idTweet") idTweet: Int): Call<Tweet?>?

    @DELETE("tweets/{idTweet}")
    fun deleteTweet(@Path("idTweet") idTweet: Int): Call<TweetDeleted?>?

    //Users
    @GET("users/profile")
    fun getProfile(): Call<ResponseUserProfile?>?

    @PUT("users/profile")
    fun updateProfile(@Body requestUserProfile: RequestUserProfile?): Call<ResponseUserProfile?>?

    @Multipart
    @POST("users/uploadprofilephoto")
    fun updloadProfilePhoto(@Part("file\"; filename=\"photo.jpeg\" ") file: RequestBody?): Call<ResponseUploadPhoto?>?

     */
}
