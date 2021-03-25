package com.offhome.app.data

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Response
import com.offhome.app.data.ActivitiesClient
import com.offhome.app.model.ActivityFromList
import javax.security.auth.callback.Callback

/**
 * Activities repository
 */
class ActivitiesRepository {
   /* var activities: MutableLiveData<List<ActivityFromList>>? = null
    private val activitiesService = ActivitiesService? = null

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        activitiesService = retrofit!!.create(ActivitiesService::class.java)
    }

    fun addActivity(activity: ActivityFromList){
        if (activity != null) val call: Call<List<ActivityFromList>> = activitiesService!!.addActivity(activity)*/
}