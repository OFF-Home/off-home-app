package com.offhome.app.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.model.JoInActivity
import com.offhome.app.ui.explore.NoActivitiesException
import com.offhome.app.data.model.*
import com.offhome.app.data.profilejson.AchievementList
import com.offhome.app.data.profilejson.UserUsername
import com.offhome.app.data.retrofit.ActivitiesClient
import com.offhome.app.data.retrofit.WeatherClient
import java.io.IOException
import java.util.ArrayList
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
    private var activities = MutableLiveData<Result<List<ActivityFromList>>>()
    private var oldActivities = MutableLiveData<Result<List<ActivityFromList>>>()
    private var likedActivities = MutableLiveData<Result<List<ActivityFromList>>>()
    private var participants: MutableLiveData<List<UserUsername>>? = null
    private var valoracio = MutableLiveData<Result<Rating>>()
    private var reviews = MutableLiveData<Result<List<ReviewOfParticipant>>>()
    private var mutableLiveData: MutableLiveData<Result<String>>? = MutableLiveData()
    private var responseJoin: MutableLiveData<String>? = MutableLiveData(" ")
    private var responseLike: MutableLiveData<Result<String>>? = MutableLiveData<Result<String>>()
    private var responseValorar: MutableLiveData<String>? = MutableLiveData(" ")
    private val activitiesClient = ActivitiesClient()
    private var activitiesService = activitiesClient.getActivitiesService()
    private val weatherClient = WeatherClient()
    private var weatherService = weatherClient.getWeatherService()
    private var suggestedactivities = MutableLiveData<Result<List<ActivityFromList>>>()
    private var friendsactivities = MutableLiveData<Result<List<ActivityFromList>>>()
    private var singleActivity: MutableLiveData<ActivityFromList>? = null

    private var achievementSet = MutableLiveData<Result<AchievementList>>()

    /**
     * This function calls the [activitiesService] in order to get all the activities in a category
     * @param categoryName is the category that we want to get the activities of
     * @return the result with a live data list of the data class ActivityFromList
     */
    fun getAll(categoryName: String): MutableLiveData<Result<List<ActivityFromList>>> {
        val call: Call<List<ActivityFromList>> = activitiesService!!.getAllActivities(categoryName)
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        activities.value = Result.Success(response.body() as List<ActivityFromList>)
                    }
                    else {
                        activities.value = Result.Success(ArrayList<ActivityFromList>())
                    }
                }
                else {
                    activities.value = Result.Error(IOException("Error getting activities"))
                }
            }

            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                // Error en la connexion
                oldActivities.value = Result.Error(IOException("Error getting old activities"))
            }
        })
        return activities
    }

    /**
     * This function calls the [activitiesService] in order to get all the old activities that a user has joined
     * @param userEmail is the email of the user
     * @return the result with a live data list of the data class ActivityFromList
     */
    fun getOldAct(userEmail: String): MutableLiveData<Result<List<ActivityFromList>>> {
        val call: Call<List<ActivityFromList>> = activitiesService!!.getOldActivities(userEmail)
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    oldActivities.value = Result.Success(response.body() as List<ActivityFromList>)
                }
                else {
                    oldActivities.value = Result.Error(IOException("Error getting old activities"))
                }
            }

            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                // Error en la connexion
                oldActivities.value = Result.Error(IOException("Error getting old activities"))
            }
        })
        return oldActivities
    }

    /**
     * This function calls the [activitiesService] in order to get all the old activities that a user has joined
     * @param userEmail is the email of the user
     * @return the result with a live data list of the data class ActivityFromList
     */
    fun getLikedAct(email: String): MutableLiveData<Result<List<ActivityFromList>>> {
        val call: Call<List<ActivityFromList>> = activitiesService!!.getLikedActivities(email)
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        likedActivities.value = Result.Success(response.body() as List<ActivityFromList>)
                    }
                    else {
                        likedActivities.value = Result.Success(ArrayList<ActivityFromList>())
                    }
                }
                else {
                    likedActivities.value = Result.Error(IOException("Error getting liked activities"))
                }
            }

            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                // Error en la connexion
                likedActivities!!.value = Result.Error(IOException("Error getting liked activities"))
            }
        })
        return likedActivities
    }


    /**
     * This function calls the [activitiesService] in order to like an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to like the activity
     * @return the result with a live data string type
     */
    fun likeActivity(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<Result<String>> {
        val join = LikeActivity(usuariCreador, dataHoraIni, usuariParticipant)
        val call = activitiesService?.likeActivity(join)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseLike?.value = Result.Success("You have liked the activity!")
                } else responseLike?.value =
                    Result.Error(IOException("There has been an error and you haven't liked the activity!"))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseLike?.value =
                    Result.Error(IOException("There has been an error and you haven't liked the activity!"))
            }
        })
        return responseLike as MutableLiveData<Result<String>>
    }

    /**
     * This function calls the [activitiesService] in order to dislike an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to dislike the activity
     * @return the result with a live data string type
     */
    fun dislikeActivity(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<Result<String>> {
        val call = activitiesService?.dislikeActivity(usuariCreador, dataHoraIni, usuariParticipant)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseLike?.value = Result.Success("You have disliked the activity!")
                } else responseLike?.value =
                    Result.Error(IOException("There has been an error and you haven't disliked the activity!"))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseLike?.value =
                    Result.Error(IOException("There has been an error and you haven't disliked the activity!"))
            }
        })
        return responseLike as MutableLiveData<Result<String>>
    }



    /**
     * This function calls the [activitiesService] in order to create the activity and set the MutableLiveData with the result
     * @param newActivity is an instance of the data class [ActivityData]
     * @return the result with a live data string type
     */
    fun addActivity(newActivity: ActivityData): MutableLiveData<Result<AchievementList>> {
        Log.d("addActivityFerran", "entrem")
        val result = MutableLiveData<Result<AchievementList>>()

        val call = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)?.let {
            activitiesService?.createActivityByUser(
                emailCreator = it,
                activitydata = newActivity
            )
        }
        call!!.enqueue(object : Callback<AchievementList> {
            override fun onResponse(
                call: Call<AchievementList>,
                response: Response<AchievementList>
            ) {
                Log.d("addActivity", "we got a response")
                if (response.isSuccessful) {
                    Log.d("addActivity", "response successful")
                    result.value = Result.Success(response.body()!!)
                } else {
                    Log.d("addActivity", "response unsuccessful")
                    result.value = Result.Error(IOException("There has been an error and the activity cannot be created"))
                }
            }

            override fun onFailure(call: Call<AchievementList>, t: Throwable) {
                Log.d("addActivity", "no response!")
                result.value = Result.Error(IOException("Error: connection failure"))
            }
        })
        Log.d("addActivity", "passo el call.enqueue")
        return result
    }

    /**
     * This function calls the [activitiesService] in order to join an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to join the activity
     * @return the result with a live data string type
     */
    fun joinActivity(usuariCreador: String, dataHoraIni: String, usuariParticipant: String, uidCreador: String, uidParticipant: String): MutableLiveData<Result<AchievementList>> {
        val result = MutableLiveData<Result<AchievementList>>()
        val join = JoInActivity(usuariCreador, dataHoraIni, usuariParticipant, uidCreador, uidParticipant)
        val call = activitiesService?.joinActivity(join)
        call!!.enqueue(object : Callback<AchievementList> {
            override fun onResponse(call: Call<AchievementList>, response: Response<AchievementList>) {
                Log.d("joinActivity", "we got a response")
                if (response.isSuccessful) {
                    Log.d("repo::joinActivity", "response.code() == " + response.code())
                    result.value = Result.Success(response.body()!!)
                } else result.value =
                    Result.Error(IOException("There has been an error and you haven't joined the activity!"))
            }

            override fun onFailure(call: Call<AchievementList>, t: Throwable) {
                result.value =
                    Result.Error(IOException("There has been an error and you haven't joined the activity!"))
            }
        })
        return result
    }

    /**
     * This function calls the [activitiesService] in order to leave an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to join the activity
     * @return the result with a live data string type
     */
    fun deleteUsuari(usuariCreador: String, dataHoraIni: String, usuariParticipant: String, uidCreador: String, uidParticipant: String): MutableLiveData<Result<String>> {
        val result = MutableLiveData<Result<String>>()
        val join = JoInActivity(usuariCreador, dataHoraIni, usuariParticipant, uidCreador, uidParticipant)
        val call = activitiesService?.deleteUsuari(join)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    result.value = Result.Success("You have left the activity :(")
                } else result.value =
                    Result.Error(IOException("There has been an error and you haven't left the activity!"))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result.value =
                    Result.Error(IOException("There has been an error and you haven't left the activity!"))
            }
        })
        return result
    }

    /**
     * This function calls the [activitiesService] in order to get suggested activities
     * @param loggedUserEmail is email of the logged user
     * @return the result the list of activities
     */
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

    /**
     * This function calls the [activitiesService] in order to get activities created by followed people
     * @param loggedUserEmail is email of the logged user
     * @return the result the list of activities
     */
    fun getFriendsActivities(loggedUserEmail: String): MutableLiveData<Result<List<ActivityFromList>>> {
        val call: Call<List<ActivityFromList>> =
            activitiesService?.getFriendsActivities(loggedUserEmail)!!
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(
                call: Call<List<ActivityFromList>>,
                response: Response<List<ActivityFromList>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        friendsactivities.value =
                            Result.Success(response.body() as List<ActivityFromList>)
                        Log.d("response", "getSuggestedActivities response: is successful")
                    } else {
                        friendsactivities.value = Result.Error(NoActivitiesException())
                    }
                } else {
                    friendsactivities.value =
                        Result.Error(IOException("getSuggestedActivities response: unsuccessful"))
                    Log.d("response", "getSuggestedActivities response: unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                friendsactivities.value =
                    Result.Error(IOException("Error getting getSuggestedActivities. communication failure (no response)"))
                Log.d(
                    "GET",
                    "Error getting getSuggestedActivities. communication failure (no response)"
                )
            }
        })
        return friendsactivities
    }
    /**
     * This function calls the [activitiesService] in order to get all the participants of an activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data string list
     */
    fun getNamesParticipants(usuariCreador: String, dataHoraIni: String): MutableLiveData<Result<List<UserUsername>>> {
        val result = MutableLiveData<Result<List<UserUsername>>>()
        val call: Call<List<UserUsername>> = activitiesService!!.getAllParticipants(usuariCreador, dataHoraIni)
        call.enqueue(object : Callback<List<UserUsername>> {
            override fun onResponse(call: Call<List<UserUsername>>, response: Response<List<UserUsername>>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body() as List<UserUsername>)
                } else {
                    result.value = Result.Error(IOException("Error getting participants"))
                }
            }

            override fun onFailure(call: Call<List<UserUsername>>, t: Throwable) {
                // Error en la connexion
                Log.d("GET", "Error getting info")
                result.value = Result.Error(IOException("Error getting participants"))
            }
        })
        return result
    }

    /**
     * This function calls the [activitiesService] in order to get all the participants of an activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data string list
     */
    fun getValoracio(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<Result<Rating>> {
        val call: Call<Rating> = activitiesService!!.getValoracioParticipant(usuariCreador, dataHoraIni, usuariParticipant)
        call.enqueue(object : Callback<Rating> {
            override fun onResponse(call: Call<Rating>, response: Response<Rating>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        valoracio.value =  Result.Success(response.body() as Rating)
                    }
                    else {
                        valoracio.value = Result.Error(IOException("Error getting valoracio participant"))
                    }
                }
                else {
                    valoracio.value = Result.Error(IOException("Error getting valoracio participant"))
                }
            }

            override fun onFailure(call: Call<Rating>, t: Throwable) {
                // Error en la connexion
                valoracio.value = Result.Error(IOException("Error getting valoracio participant"))
            }
        })
        return valoracio
    }

    /**
     * This function calls the [activitiesService] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to join the activity
     * @return the result with a live data string type
     */
    fun valorarActivitat(usuariParticipant: String, usuariCreador: String, dataHoraIni: String, valoracio: Int, comentari: String): MutableLiveData<Result<AchievementList>> {
        val result = MutableLiveData<Result<AchievementList>>()
        val rate = RatingSubmission(usuariParticipant, usuariCreador, dataHoraIni, valoracio, comentari)
        val call = activitiesService?.addReview(rate)
        call!!.enqueue(object : Callback<AchievementList> {
            override fun onResponse(call: Call<AchievementList>, response: Response<AchievementList>) {
                if (response.isSuccessful) {
                    //responseValorar?.value = "Your rating has been saved"
                    Log.d("valorarActivitat", "response successful")
                    result.value = Result.Success(response.body()!!)
                } else {
                   // responseValorar?.value = "There has been an error and your rating could not be saved!"
                    Log.d("valorarActivitat", "response unsuccessful")
                    result.value = Result.Error(IOException("There has been an error and your rating could not be saved!"))
                }
            }
            override fun onFailure(call: Call<AchievementList>, t: Throwable) {
                //responseValorar?.value = "There has been an error and your rating could not be saved!"
                Log.d("valorarActivitat", "no response!")
                result.value = Result.Error(IOException("There has been an error and your rating could not be saved!"))
            }
        })
        return result
    }

    /**
     * This function calls the [activitiesService] in order to get all the reviews (with their authors) of an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data list of the data class ReviewOfParticipant
     */
    fun getCommentsParticipants(usuariCreador: String, dataHoraIni: String): MutableLiveData<Result<List<ReviewOfParticipant>>> {
        val call: Call<List<ReviewOfParticipant>> = activitiesService!!.getAllReviews(usuariCreador, dataHoraIni)
        call.enqueue(object : Callback<List<ReviewOfParticipant>> {
            override fun onResponse(call: Call<List<ReviewOfParticipant>>, response: Response<List<ReviewOfParticipant>>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        reviews.value =  Result.Success(response.body() as List<ReviewOfParticipant>)
                    }
                    else {
                        reviews.value = Result.Error(IOException("Error getting reviews"))
                    }
                }
                else {
                    valoracio.value = Result.Error(IOException("Error getting reviews"))
                }
            }
            override fun onFailure(call: Call<List<ReviewOfParticipant>>, t: Throwable) {
                // Error en la connexion
                valoracio.value = Result.Error(IOException("Error getting reviews"))
            }
        })
        return reviews
    }

    fun getActivitiesByDescTitle(): MutableLiveData<List<ActivityFromList>> {
        var listSorted: MutableLiveData<List<ActivityFromList>>? = null
        if (listSorted == null) listSorted = MutableLiveData<List<ActivityFromList>>()
        val call: Call<List<ActivityFromList>> = activitiesService!!.getActivitiesByDescTitle()
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    listSorted.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                Log.d("GET", "Error getting list of activities in descending order")
            }
        })
        return listSorted
    }

    fun getActivitiesByAscTitle(): MutableLiveData<List<ActivityFromList>> {
        var listSorted: MutableLiveData<List<ActivityFromList>>? = null
        if (listSorted == null) listSorted = MutableLiveData<List<ActivityFromList>>()
        val call: Call<List<ActivityFromList>> = activitiesService!!.getActivitiesByAscTitle()
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    listSorted.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                Log.d("GET", "Error getting list of activities in ascending order")
            }
        })
        return listSorted
    }

    fun getActivitiesByDate(): MutableLiveData<List<ActivityFromList>> {
        var listSorted: MutableLiveData<List<ActivityFromList>>? = null
        if (listSorted == null) listSorted = MutableLiveData<List<ActivityFromList>>()
        val call: Call<List<ActivityFromList>> = activitiesService!!.getActivitiesByDate()
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(
                call: Call<List<ActivityFromList>>,
                response: Response<List<ActivityFromList>>
            ) {
                if (response.isSuccessful) {
                    listSorted.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                Log.d("GET", "Error getting list of activities by date")
            }
        })
        return listSorted
    }

    fun getActivityResult(activityCreator: String, activityDateTime: String): MutableLiveData<Result<ActivityFromList>> {
        val result = MutableLiveData<Result<ActivityFromList>>()
        // val activityCreator = "agnesmgomez@gmail.com"; val activityDateTime = "2021-5-29 23:59:00"
        Log.d("making dynLink call", "activityCreator = " + activityCreator + " activityDateTime = " + activityDateTime)

        val call: Call<ActivityFromList> = activitiesService!!.getActivity(activityCreator, activityDateTime)
        call.enqueue(object : Callback<ActivityFromList> {
            override fun onResponse(call: Call<ActivityFromList>, response: Response<ActivityFromList>) {
                if (response.isSuccessful) {
                    Log.d("repo::getActivityResult", "response.code() == " + response.code())
                    if (response.code() == 200 || response.code() ==201) {
                        if (response.body() == null)
                            Log.d("repo::getActivityResult", "response.body() == null. (a back retornen 204). no hauria d'arribar aqui")

                        Log.d("response", "getActivityResult response: is successful")
                        result.value = Result.Success(response.body()!!)
                    }
                    else {
                        Log.d("repo::getActivityResult", "response code not in (200, 201)")
                    }
                } else {
                    Log.d("response", "getActivityResult response: unsuccessful")
                    result.value = Result.Error(IOException("getActivityResult Error: unsuccessful"))
                }
            }

            override fun onFailure(call: Call<ActivityFromList>, t: Throwable) {
                Log.d("no response", "getActivityResult no response")
                result.value = Result.Error(IOException("getActivityResult Error: failure", t))
            }
        })

        return result
    }

    fun getWeather(): MutableLiveData<Result<Tiempo>> {
        val result = MutableLiveData<Result<Tiempo>>()
        val call: Call<Tiempo> = weatherService!!.getWeather()
        call.enqueue(object : Callback<Tiempo> {
            override fun onResponse(call: Call<Tiempo>, response: Response<Tiempo>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body() as Tiempo)
                }
                else {
                    result.value = Result.Error(IOException("Error getting info"))
                }
            }

            override fun onFailure(call: Call<Tiempo>, t: Throwable) {
                result.value = Result.Error(IOException("Error getting info"))
            }
        })
                
        return result
    }
    
    /**
     * It gets the activities by distance
     */
    fun getActivitiesByRadi(
        categoryName: String,
        latitude: Double,
        longitude: Double,
        progress: Int
    ): MutableLiveData<Result<List<ActivityFromList>>> {
        val result = MutableLiveData<Result<List<ActivityFromList>>>()
        val call: Call<List<ActivityFromList>> = activitiesService!!.getActivitiesByRadi(latitude, longitude, progress)
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    if (response.code() == 200)
                        result.value = Result.Success(response.body() as List<ActivityFromList>)
                    else result.value = Result.Success(ArrayList<ActivityFromList>())
                } else result.value = Result.Error(IOException("Error getting activities"))
            }

            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                // Error en la connexion
                result.value = Result.Error(IOException("Error getting activities"))
                Log.d("GET", "Error getting activities")
            }
        })
        return result
    }
}
