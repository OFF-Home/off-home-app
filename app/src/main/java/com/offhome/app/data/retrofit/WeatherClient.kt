package com.offhome.app.data.retrofit

import com.offhome.app.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient {
    private lateinit var instance: WeatherClient
    private var weatherService: WeatherService? = null
    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherService = retrofit!!.create(WeatherService::class.java)
    }

    fun getInstance(): WeatherClient? {
        return instance
    }

    fun getWeatherService(): WeatherService? {
        return weatherService
    }
}
