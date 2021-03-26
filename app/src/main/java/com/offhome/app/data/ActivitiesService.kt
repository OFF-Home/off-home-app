package com.offhome.app.data

import com.offhome.app.model.ActivityData
import okhttp3.ResponseBody
import retrofit2.Call
import com.offhome.app.model.ActivityFromList
import retrofit2.http.*

interface ActivitiesService {

    /**
     * This call is for creating a new activity
     */
    @POST("activitats/create/{emailCreator}")
    fun createActivityByUser(@Path("emailCreator") emailCreator: String, @Body activitydata: ActivityData): Call<ResponseBody>
  
    @GET("categories/{category}")
    fun getAllActivities(@Path("category") categoryName: String): Call<List<ActivityFromList>>
}