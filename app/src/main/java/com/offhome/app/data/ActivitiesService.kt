package com.offhome.app.data

import com.offhome.app.model.ActivityFromList
import retrofit2.Call
import retrofit2.http.*

interface ActivitiesService {
    @GET("categories/{category}")
    fun getAllActivities(@Path("category") categoryName: String): Call<List<ActivityFromList>>

    /*@POST("activities")
    fun addActivity(activity: ActivityFromList)
    */
}