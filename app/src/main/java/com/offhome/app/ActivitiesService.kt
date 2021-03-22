package com.offhome.app

import com.offhome.app.model.ActivityFromList
import retrofit2.Call
import retrofit2.http.*

interface ActivitiesService {
    @GET("activities")
    fun getAllActivities(): Call<List<ActivityFromList>>

}