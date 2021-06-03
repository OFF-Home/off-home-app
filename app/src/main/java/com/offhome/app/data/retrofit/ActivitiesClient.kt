
package com.offhome.app.data.retrofit

import com.offhome.app.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivitiesClient {
    private lateinit var instance: ActivitiesClient
    private var activitiesService: ActivitiesService? = null
    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(Constants().BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        activitiesService = retrofit!!.create(ActivitiesService::class.java)
    }

    fun getInstance(): ActivitiesClient? {
        return instance
    }

    fun getActivitiesService(): ActivitiesService? {
        return activitiesService
    }
}
