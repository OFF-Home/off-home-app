package com.offhome.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivitiesClient {
    private lateinit var instance: ActivitiesClient
    private var activitiesService: ActivitiesService? = null
    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/categories/:nom_categoria/")
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