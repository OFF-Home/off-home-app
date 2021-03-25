package com.offhome.app.data

import android.util.Log
import com.offhome.app.model.ActivityInfo
import com.offhome.app.ui.infoactivity.InfoActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoActivityRespository {
    /*
    var infoactivities: ActivityInfo? = null
    private val infoActivityClient = InfoActivityClient()
    private val infoactivitiesService = infoActivityClient.getInfoActivityService()


    fun getActivity(): ActivityInfo {
        if (infoactivities == null) infoactivities = ActivityInfo()
        val call: Call<ActivityInfo> = infoactivitiesService!!.getActivity()
        call.enqueue(object : Callback<ActivityInfo> {
            override fun onResponse(call: Call<ActivityInfo>, response: Response<ActivityInfo>) {
                if (response.isSuccessful) {
                    infoactivities!!.value = response.body()
                }
            }

            override fun onFailure(call: Call<ActivityInfo>, t: Throwable) {
                // Error en la connexion
                Log.d("GET", "Erro getting info")
            }
        })
        return infoactivities as ActivityInfo
    }

     */
}