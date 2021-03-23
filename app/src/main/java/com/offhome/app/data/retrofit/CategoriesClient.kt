package com.offhome.app.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriesClient {
    private lateinit var instance: CategoriesClient
    private var categoriesService: CategoriesService? = null
    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        categoriesService = retrofit!!.create(CategoriesService::class.java)
    }

    fun getInstance(): CategoriesClient {
        return instance
    }

    fun getCategoriesService(): CategoriesService? {
        return categoriesService
    }
}
