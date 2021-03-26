package com.offhome.app.data

import com.offhome.app.model.ActivityData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivitiesService {

    /**
     * This call is for creating a new activity
     */
    @POST("activitats/create/{emailCreator}")
    fun createActivityByUser(@Path("emailCreator") emailCreator: String, @Body activitydata: ActivityData): Call<ResponseBody>
}