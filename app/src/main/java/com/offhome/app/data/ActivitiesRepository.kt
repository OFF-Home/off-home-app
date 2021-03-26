package com.offhome.app.data


import androidx.lifecycle.MutableLiveData
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.ActivityData
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.offhome.app.data.model.JoInActivity
import retrofit2.Call

/**
 * This class requests the response of the creation of the activities
 * @author Maria Nievas Viñals
 * @property mutableLiveData where the result from creating the activity is saved
 * @property activitiesClient references the instance of the [ActivitiesClient] class
 * @property activitiesService  references the instance initialized of the [ActivitiesService] class from the [ActivitiesClient] class
 *
 */
class ActivitiesRepository {
    private var activities : MutableLiveData<List<ActivityFromList>>? = null
    private var mutableLiveData : MutableLiveData<String>? = MutableLiveData(" ")
    private var responseJoin : MutableLiveData<String>? = MutableLiveData(" ")
    private val activitiesClient = ActivitiesClient()
    private var activitiesService = activitiesClient.getActivitiesService()

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

    /**
     * This function calls the [activitiesService] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to join the activity
     * @return the result with a live data string type
     */
    fun joinActivity(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<String> {
        val join = JoInActivity(usuariCreador, dataHoraIni, usuariParticipant)
        val call = activitiesService?.joinActivity(join)
        call!!.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseJoin?.value = "You have joined the activity!"
                } else responseJoin?.value =
                    "It has been an error and you haven't joined the activity!"
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseJoin?.value =
                    "It has been an error and you haven't joined the activity!"
            }
        })
        return responseJoin as MutableLiveData<String>
    }
}