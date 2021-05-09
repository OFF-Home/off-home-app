package com.offhome.app.data

import com.offhome.app.data.model.JoInActivity
import com.offhome.app.model.ActivityData
import com.offhome.app.model.ActivityFromList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ActivitiesService {

    /**
     * This call is for creating a new activity
     */
    @POST("activitats/create/{emailCreator}")
    fun createActivityByUser(@Path("emailCreator") emailCreator: String, @Body activitydata: ActivityData): Call<ResponseBody>

    @GET("categories/{category}")
    fun getAllActivities(@Path("category") categoryName: String): Call<List<ActivityFromList>>

    /**
     * This call is for joining an activity
     */
    @POST("/activitats/insertusuari")
    fun joinActivity(@Body join: JoInActivity): Call<ResponseBody>

    /**
     * This call is to leave an activity
     */
    @POST("/activitats/deleteUsuari")
    fun deleteUsuari(@Body join: JoInActivity): Call<ResponseBody>


    /**
     * This call is to get suggested activities
     */
    @GET("/activitats/lolazo") //TODO: posar la ruta correcte
    fun getSuggestedActivities(@Path("email") loggedUserEmail: String): Call<List<ActivityFromList>>
}
