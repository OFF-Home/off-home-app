package com.offhome.app.data.retrofit



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Categories Client Class
 * It has the base configuration of retrofit for getting the categories
 * @property instance has the instance of this client
 * @property categoriesService is the instance of the service that connects with backend
 * @property retrofit is the instance of retrofit library to reach backend
 */
class CategoriesClient {
    private lateinit var instance: CategoriesClient
    private var categoriesService: CategoriesService? = null
    private var retrofit: Retrofit? = null

    /**
     * Inits all configuration, such as the baseURL or the GSon converter
     */
    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-52-3-247-204.compute-1.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        categoriesService = retrofit!!.create(CategoriesService::class.java)
    }

    /**
     * Returns the instance of the client
     */
    fun getInstance(): CategoriesClient {
        return instance
    }

    /**
     * Returns the instance of the service
     */
    fun getCategoriesService(): CategoriesService? {
        return categoriesService
    }
}
