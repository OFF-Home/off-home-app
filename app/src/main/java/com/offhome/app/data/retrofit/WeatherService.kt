package com.offhome.app.data.retrofit

import com.offhome.app.data.model.Tiempo
import retrofit2.Call
import retrofit2.http.GET

interface WeatherService {
    @GET("forecast?q=Barcelona,Es&appid=2bad5bca6c65128d7d2bb9d092d0e023&units=metric")
    fun getWeather(): Call<Tiempo>
}
