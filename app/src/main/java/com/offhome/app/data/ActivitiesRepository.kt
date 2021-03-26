package com.offhome.app.data

import android.telecom.Call
import androidx.lifecycle.MutableLiveData
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.ActivityData
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Activities repository
 */
class ActivitiesRepository {
    private var mutableLiveData : MutableLiveData<String>? = MutableLiveData(" ")
    private val activitiesClient = ActivitiesClient()
    private var activitiesService = activitiesClient.getActivitiesService()

    fun addActivity(newActivity: ActivityData): MutableLiveData<String> {
        val call = activitiesService?.createActivityByUser(emailCreator = "victorfer@gmai.com", newActivity)
        call!!.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    mutableLiveData?.value = "Activity created!"
                } else mutableLiveData?.value =
                    "It has been an error and the activity could not be created"
            }
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                mutableLiveData?.value =
                    "It has been an error and the activity could not be created"
            }
        })
        return mutableLiveData as MutableLiveData<String>
    }
}