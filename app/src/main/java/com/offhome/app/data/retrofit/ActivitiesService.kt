package com.offhome.app.data.retrofit



import com.offhome.app.data.model.JoInActivity
import com.offhome.app.data.profilejson.UserUsername
import com.offhome.app.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ActivitiesService {

    /**
     * This call is for creating a new activity
     */
    @POST("activitats/create/{usuariCreador}")
    fun createActivityByUser(@Path("usuariCreador") uidCreator: String, @Body activitydata: ActivityData): Call<ResponseBody>

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
    @GET("/activitats/participants/{usuariCreador}")
    fun getAllParticipants(@Path("usuariCreador") usuariCreador: String, @Query("dataHoraIni") dataHoraIni: String): Call<List<UserUsername>>

    /**
     * This call is to review an activity
     */
    @PUT("/activitats/valorar")
    fun addReview(@Body rate: RatingSubmission): Call<ResponseBody>

    /**
     * This call is to get the rating of the user on an activity
     */
    @GET("/activitats/participants/valoracio")
    fun getValoracioParticipant(
        @Query("usuariCreador") usuariCreador: String,
        @Query("dataHoraIni") dataHoraIni: String,
        @Query("usuariParticipant") usuariParticipant: String
    ): Call<Rating>

    /**
     * This call is to get all the reviews of an activity (with their authors)
     */
    @GET("activitats/participants/comentaris")
    fun getAllReviews(
        @Query("usuariCreador") usuariCreador: String,
        @Query("dataHoraIni") dataHoraIni: String
    ): Call<List<ReviewOfParticipant>>

    //gets a single activity identified by its creator and date
    @GET("/activitats/{username}/{datahora}")
    fun getActivity(@Path("username") activityCreator: String, @Path("datahora") activityDateTime: String): Call<ActivityFromList>
}
