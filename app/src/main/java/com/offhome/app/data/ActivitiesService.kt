package com.offhome.app.data

import com.offhome.app.data.model.JoInActivity
import com.offhome.app.model.ActivityData
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.Rating
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ActivitiesService {

    /**
     * This call is for creating a new activity
     */
    @POST("activitats/create/{emailCreator}")
    fun createActivityByUser(@Path("emailCreator") emailCreator: String, @Body activitydata: ActivityData): Call<ResponseBody>

    @GET("categories/{category}")
    fun getAllActivities(@Path("category") categoryName: String): Call<List<ActivityFromList>>

    /**
     * This call is for joining an activity
     */
    @POST("/activitats/insertusuari")
    fun joinActivity(@Body join: JoInActivity): Call<ResponseBody>

    /**
     * This call is to leave an activity
     */
    @POST("/activitats/deleteUsuari")
    fun deleteUsuari(@Body join: JoInActivity): Call<ResponseBody>

    /**
     * This call is to get all the participants of an activity
     */
    @HTTP(method = "GET", path = "activitats/participants/{usuariCreador}", hasBody = true)
    fun getAllParticipants(@Path("usuariCreador") usuariCreador: String, @Body dataHoraIni: String): Call<List<String>>

    /**
     * This call is to review an activity
     */
    @PUT("/activitats/valorar")
    fun addReview(@Body usuariParticipant: String, usuariCreador: String, dataHoraIni: String, valoracio: Int, comentari: String): Call<ResponseBody>

    /**
     * This call is to get the rating of the user on an activity
     */
    @GET("/activitats/participants/valoracio")
    fun getValoracioParticipant(@Query("usuariCreador") usuariCreador: String, @Query("dataHoraIni") dataHoraIni: String,
                                @Query("usuariParticipant") usuariParticipant: String): Call<Rating>

}
