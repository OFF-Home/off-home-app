package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.ActivitiesClient
import com.offhome.app.model.ActivityFromList

class ActivitiesRepository {
    var activities: MutableLiveData<List<ActivityFromList>>? = null
    private val activitiesClient = ActivitiesClient()

    /*private val activitiesService = activitiesClient.getActivitiesService()

    fun getAll(): MutableLiveData<List<ActivityFromList>> {
        if (activities == null) activities = MutableLiveData<List<ActivityFromList>>()
        val call: Call<List<ActivityFromList>> = activitiesService!!.getAllActivities()
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    activities!!.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                // Error en la connexion
                Log.d("GET", "Erro getting info")
            }
        })

        return activities as MutableLiveData<List<ActivityFromList>>
    }*/
}