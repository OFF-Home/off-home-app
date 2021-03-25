package com.offhome.app.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoActivityClient {
    private lateinit var instance: InfoActivityClient
    private var infoActivityService: InfoActivityService? = null
    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/activities/:email/:datahora/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        infoActivityService = retrofit!!.create(InfoActivityService::class.java)
    }

    fun getInstance(): InfoActivityClient? {
        return instance
    }

    fun getInfoActivityService(): InfoActivityService? {
        return infoActivityService
    }
}