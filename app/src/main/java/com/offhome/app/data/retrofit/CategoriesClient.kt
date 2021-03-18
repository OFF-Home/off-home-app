package com.offhome.app.data.retrofit

import android.provider.SyncStateContract
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriesClient {
    private lateinit var instance: CategoriesClient
    private var categoriesService: CategoriesService? = null
    private var retrofit: Retrofit? = null

    fun CategoriesClient() {
        retrofit = Retrofit.Builder()
            .baseUrl("htpps://")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        categoriesService = retrofit!!.create(CategoriesService::class.java)
    }

    fun getInstance(): CategoriesClient? {
        return instance
    }

    fun getCategoriesService(): CategoriesService? {
        return categoriesService
    }
}