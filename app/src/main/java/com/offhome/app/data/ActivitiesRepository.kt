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
 * This class requests the response of the creation of the activities
 * @author Maria Nievas Viñals
 * @property mutableLiveData where the result from creating the activity is saved
 * @property activitiesClient references the instance of the [ActivitiesClient] class
 * @property activitiesService  references the instance initialized of the [ActivitiesService] class from the [ActivitiesClient] class
 *
 */
class ActivitiesRepository {
    private var mutableLiveData : MutableLiveData<String>? = MutableLiveData(" ")
    private val activitiesClient = ActivitiesClient()
    private var activitiesService = activitiesClient.getActivitiesService()

    /**
     * This function calls the [activitiesService] in order to create the activity and set the MutableLiveData with the result
     * @param newActivity is an instance of the data class [ActivityData]
     * @return the result with a live data string type
     */
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