package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.ActivitiesClient
import com.offhome.app.model.ActivityFromList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activities repository
 */
class ActivitiesRepository {
    var activities: MutableLiveData<List<ActivityFromList>>? = null
    private val activitiesClient = ActivitiesClient()
    private val activitiesService = activitiesClient.getActivitiesService()

    fun getAll(categoryName: String): MutableLiveData<List<ActivityFromList>> {
        if (activities == null) activities = MutableLiveData<List<ActivityFromList>>()
        val call: Call<List<ActivityFromList>> = activitiesService!!.getAllActivities(categoryName)
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
    }
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