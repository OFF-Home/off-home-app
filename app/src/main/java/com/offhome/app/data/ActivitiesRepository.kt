package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.model.JoInActivity
import com.offhome.app.model.ActivityData
import com.offhome.app.model.ActivityFromList
import com.offhome.app.ui.explore.NoActivitiesException
import java.io.IOException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class requests the response of the creation of the activities
 * @author Maria Nievas Viñals
 * @property mutableLiveData where the result from creating the activity is saved
 * @property activitiesClient references the instance of the [ActivitiesClient] class
 * @property activitiesService  references the instance initialized of the [ActivitiesService] class from the [ActivitiesClient] class
 *
 */
class ActivitiesRepository {
    private var activities: MutableLiveData<List<ActivityFromList>>? = null
    private var mutableLiveData: MutableLiveData<String>? = MutableLiveData(" ")
    private var responseJoin: MutableLiveData<String>? = MutableLiveData(" ")
    private val activitiesClient = ActivitiesClient()
    private var activitiesService = activitiesClient.getActivitiesService()
    private var suggestedactivities = MutableLiveData<Result<List<ActivityFromList>>>()
    private var friendsactivities = MutableLiveData<Result<List<ActivityFromList>>>()

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
        val call = activitiesService?.createActivityByUser(emailCreator = "victorfer@gmai.com",
            activitydata = newActivity
        )
        call!!.enqueue(object : Callback<ResponseBody> {
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
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseJoin?.value = "You have joined the activity!"
                } else responseJoin?.value =
                    "There has been an error and you haven't joined the activity!"
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseJoin?.value =
                    "There has been an error and you haven't joined the activity!"
            }
        })
        return responseJoin as MutableLiveData<String>
    }

    /**
     * This function calls the [activitiesService] in order to leave an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to join the activity
     * @return the result with a live data string type
     */
    fun deleteUsuari(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<String> {
        val join = JoInActivity(usuariCreador, dataHoraIni, usuariParticipant)
        val call = activitiesService?.deleteUsuari(join)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseJoin?.value = "You have left the activity :("
                } else responseJoin?.value =
                    "There has been an error and you haven't left the activity!"
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseJoin?.value =
                    "There has been an error and you haven't left the activity!"
            }
        })
        return responseJoin as MutableLiveData<String>
    }

    fun getSuggestedActivities(loggedUserEmail: String): MutableLiveData<Result<List<ActivityFromList>>> {
        val call: Call<List<ActivityFromList>> = activitiesService?.getSuggestedActivities(loggedUserEmail)!!
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    suggestedactivities.value = Result.Success(response.body() as List<ActivityFromList>)
                    Log.d("response", "getSuggestedActivities response: is successful")
                } else {
                    suggestedactivities.value = Result.Error(IOException("getSuggestedActivities response: unsuccessful"))
                    Log.d("response", "getSuggestedActivities response: unsuccessful")
                }
            }
            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                suggestedactivities.value = Result.Error(IOException("Error getting getSuggestedActivities. communication failure (no response)"))
                Log.d("GET", "Error getting getSuggestedActivities. communication failure (no response)")
            }
        })
        return suggestedactivities

    }

    fun getFriendsActivities(loggedUserEmail: String): MutableLiveData<Result<List<ActivityFromList>>> {
        //return MutableLiveData(Result.Success(listOf(ActivityFromList("pau.cuesta@gmail.com", "Claris", 1, "2021-06-26 18:00:00.000", "Running", 15, "Running per Montserrat", "Anirem a correr fins a Montserrat des de Barcelona", "2021-06-26 21:00:00.000"), ActivityFromList("pau.cuesta@gmail.com", "Claris", 1, "2021-06-26 18:00:00.000", "Running", 15, "Running per Montserrat", "Anirem a correr fins a Montserrat des de Barcelona", "2021-06-26 21:00:00.000"), ActivityFromList("pau.cuesta@gmail.com", "Claris", 1, "2021-06-26 18:00:00.000", "Running", 15, "Running per Montserrat", "Anirem a correr fins a Montserrat des de Barcelona", "2021-06-26 21:00:00.000"))))

        val call: Call<List<ActivityFromList>> = activitiesService?.getFriendsActivities(loggedUserEmail)!!
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        friendsactivities.value = Result.Success(response.body() as List<ActivityFromList>)
                        Log.d("response", "getSuggestedActivities response: is successful")
                    } else {
                        friendsactivities.value = Result.Error(NoActivitiesException())
                    }
                } else {
                    friendsactivities.value = Result.Error(IOException("getSuggestedActivities response: unsuccessful"))
                    Log.d("response", "getSuggestedActivities response: unsuccessful")
                }
            }
            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                friendsactivities.value = Result.Error(IOException("Error getting getSuggestedActivities. communication failure (no response)"))
                Log.d("GET", "Error getting getSuggestedActivities. communication failure (no response)")
            }
        })
        return friendsactivities
    }
}
