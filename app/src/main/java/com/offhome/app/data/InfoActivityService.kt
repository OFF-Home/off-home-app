package com.offhome.app.data

import com.offhome.app.model.ActivityInfo
import retrofit2.Call
import retrofit2.http.*

interface InfoActivityService {
    @GET("infoactivity")
    fun getActivity(emailUsuari: String, dataTimeIni: String): Call<ActivityInfo>
}