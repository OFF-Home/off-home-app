package com.offhome.app.model.profile



import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.FollowUnfollow
import com.offhome.app.data.model.FollowingUser
import com.offhome.app.data.profilejson.NomTag
import com.offhome.app.data.profilejson.UserDescription
import com.offhome.app.data.profilejson.UserUsername
import com.offhome.app.data.retrofit.UserClient
import com.offhome.app.model.ActivityFromList
import java.io.File
import java.io.IOException
import java.io.InputStream
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Class *ProfileRepository*
 *
 * Repository for the Profile screen. Plays the "Model" role in this screen's MVVM
 * Encapsulates the access to data from the ViewModel
 *
 * @author Ferran, Pau, others
 * @property userClient reference to the userClient object
 * @property userService reference to the userService object
 * @property userInfo mutable live data of the user info obtained
 * @property activities mutable live data of the activities obtained
 * @property tags mutable live data of the tags obtained
 * @property usernameSetSuccessfully mutable live data of the response to the call to set the username
 * @property descriptionSetSuccessfully mutable live data of the response to the call to set the description
 * @property tagDeletedSuccessfully mutable live data of the response to the call to delete a tag
 * @property tagAddedSuccessfully mutable live data of the response to the call to add a tag
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileRepository {

    private val userClient = UserClient()
    private var userService = userClient.getUserService()
    var userInfo: MutableLiveData<UserInfo>? = null
    var usernameSetSuccessfully: MutableLiveData<ResponseBody>? = null
    var descriptionSetSuccessfully: MutableLiveData<ResponseBody> = MutableLiveData<ResponseBody>()
    var tagDeletedSuccessfully: MutableLiveData<ResponseBody> = MutableLiveData<ResponseBody>()
    var tagAddedSuccessfully: MutableLiveData<ResponseBody> = MutableLiveData<ResponseBody>()
    var activities: MutableLiveData<List<ActivityFromList>>? = null
    var tags: MutableLiveData< List<TagData> >? = null
    var followedUsers: MutableLiveData<List<UserInfo>>? = null

    /**
     * obtains ProfileInfo from the lower level
     *
     * does the GET call and observes the result
     * @param email email of the user whose data is to be obtained
     * @return mutable live data which will be updated with the data from the call, if it is successful
     */
    fun getProfileInfo(email: String): MutableLiveData<Result<UserInfo>> {

        val userInfo = MutableLiveData<Result<UserInfo>>() // linea afegida perque no peti. la he copiat de ActivitiesRepository

        // accés a Backend
        val call: Call<UserInfo> = userService!!.getProfileInfo(email)
        call.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    userInfo.value = Result.Success(response.body() as UserInfo)
                    Log.d("success response", "getProfileInfo: got a response indicating success")
                } else {
                    userInfo!!.value = Result.Error(IOException(response.errorBody().toString()))
                    Log.d("failure response", "getProfileInfo: got a response indicating failure")
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                userInfo.value = Result.Error(IOException(t.message))
                Log.d("GET", "Error getting ProfileInfo. communication failure (no response)")
            }
        })
        return userInfo
    }

    /**
     * obtains the user's activities from the lower level
     *
     * does the GET call and observes the result
     * @param email key of the user whose activities are to be obtained
     * @return mutable live data which will be updated with the data from the call, if it is successful
     */
    fun getUserActivities(email: String): MutableLiveData<List<ActivityFromList>> {
        if (activities == null) activities = MutableLiveData<List<ActivityFromList>>()

        val call: Call<List<ActivityFromList>> = userService!!.getUserActivities(email)
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    activities!!.value = response.body()
                    Log.d("response", "getUserActivities response: is successful")
                } else {
                    Log.d("response", "getUserActivities response: unsuccessful")
                }
            }
            override fun onFailure(call: Call<List<ActivityFromList>>, t: Throwable) {
                Log.d("GET", "Error getting getUserActivities. communication failure (no response)")
            }
        })
        return activities as MutableLiveData<List<ActivityFromList>>
    }

    /**
     * obtains the user's tags from the lower level
     *
     * does the GET call and observes the result
     * @param email key of the user whose tags are to be obtained
     * @return mutable live data which will be updated with the data from the call, if it is successful
     */
    fun getUserTags(email: String): MutableLiveData<List<TagData>> {
        if (tags == null) tags = MutableLiveData< List<TagData> >()

        val call: Call<List<TagData>> = userService!!.getTags(email = email)
        call.enqueue(object : Callback< List<TagData> > {
            override fun onResponse(call: Call< List<TagData> >, response: Response< List<TagData> >) {
                if (response.isSuccessful) {
                    tags!!.value = response.body()
                    Log.d("response", "getUserTags response: is successful")
                } else {
                    Log.d("response", "getUserTags response: unsuccessful")
                }
            }
            override fun onFailure(call: Call< List<TagData> >, t: Throwable) {
                Log.d("GET", "Error getting getUserTags. communication failure (no response)")
            }
        })
        return tags as MutableLiveData<List<TagData>>
    }
    // Versio millor, en procés. Si la acabo i funciona, faltarà adaptar el ProfileFragment a aixo.
    fun getUserTagsResult(email: String): MutableLiveData<Result<List<TagData>>> {
        Log.d("comença getUserTagsRes", "email = " + email)
        val result = MutableLiveData<Result<List<TagData>>>()

        val call: Call<List<TagData>> = userService!!.getTags(email = email)
        call.enqueue(object : Callback< List<TagData> > {
            override fun onResponse(call: Call< List<TagData> >, response: Response< List<TagData> >) {
                if (response.isSuccessful) {
                    Log.d("repo::getUserTagsResult", "response.code() == " + response.code())
                    if (response.code() == 200) {
                        if (response.body() == null)
                            Log.d("repo::getUserTagsResult", "response.body() == null.")

                        Log.d("response", "getUserTagsResult response: is successful")
                        result.value = Result.Success(response.body()!!)
                    }
                } else {
                    Log.d("response", "getUserTagsResult response: unsuccessful")
                    result.value = Result.Error(IOException("getUserTagsResult Error: unsuccessful"))
                }
            }
            override fun onFailure(call: Call< List<TagData> >, t: Throwable) {
                Log.d("GET", "Error getting getUserTagsResult. communication failure (no response)", t)
                result.value = Result.Error(IOException("getUserTagsResult Error: failure", t))
            }
        })
        return result
    }

    // per quan agafem la profilePic de backend.
    // es veu que no ens caldrà perquè tenim Glide instal·lat!
    private fun getBitmapFromURL(url1: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val inputStream: InputStream = java.net.URL(url1).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    // podria funcionar sense tot el rollo de nulls, com setDescription
    /**
     * Propagates the setUsername process to the lower level
     *
     * does the POST call and observes the result
     * Sets the Repository's usernameSetSuccessfully live data with the response of the call
     * @param email key of the user whose username is to be set
     * @param newUsername username to set
     * @return mutable live data which will be updated with the result of the call
     */
    fun setUsername(email: String, newUsername: String): MutableLiveData<ResponseBody> {
        if (usernameSetSuccessfully == null) usernameSetSuccessfully = MutableLiveData<ResponseBody>() // linea afegida perque no peti.
        val call: Call<ResponseBody> = userService!!.setUsername(email = email, username = UserUsername(username = newUsername)) // o algo tipo updateUser()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                usernameSetSuccessfully!!.value = response.body()
                if (response.isSuccessful) {
                    // la crida retorna 200 encara que sigui user not found.
                    Log.d("response", "setUsername response: is successful")
                    // usernameSetSuccessfully!!.value = true
                } else { // si rebem resposta de la BD pero ens informa d'un error
                    Log.d("response", "setUsername response: unsuccessful")
                    // usernameSetSuccessfully!!.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "setUsername no response")
                t.printStackTrace()
                Log.w("no response", "setUsername no response", t.cause)
                usernameSetSuccessfully!!.value = ResponseBody.create(null, "no response")
            }
        })

        return usernameSetSuccessfully as MutableLiveData<ResponseBody>
    }

    // funciona sense tot el rollo de nulls
    /**
     * Propagates the setDescription process to the lower level
     *
     * does the POST call and observes the result
     * Sets the Repository's descriptionSetSuccessfully live data with the response of the call
     * @param email key of the user whose description is to be set
     * @param newDescription description to set
     * @return mutable live data which will be updated with the result of the call
     */
    fun setDescription(email: String, newDescription: String): MutableLiveData<ResponseBody> {
        val call: Call<ResponseBody> = userService!!.setDescription(email = email, description = UserDescription(description = newDescription))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                descriptionSetSuccessfully.value = response.body()
                descriptionSetSuccessfully.postValue(response.body())
                if (response.isSuccessful) {
                    Log.d("response", "setDescription response: is successful")
                } else {
                    Log.d("response", "setDescription response: unsuccessful")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "setDescription no response")
                t.printStackTrace()
                Log.w("no response", "setDescription no response", t.cause)
                descriptionSetSuccessfully.value = ResponseBody.create(null, "no response")
            }
        })
        return descriptionSetSuccessfully
    }
    fun setDescription2(email: String, newDescription: String): MutableLiveData<Result<String>> {
        val result = MutableLiveData<Result<String>>()

        val call: Call<ResponseBody> = userService!!.setDescription(email = email, description = UserDescription(description = newDescription))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("response", "setDescription response: is successful")
                    result.value = Result.Success(response.body().toString())
                } else {
                    Log.d("response", "setDescription response: unsuccessful")
                    result.value = Result.Error(IOException("setDescription2 Error: unsuccessful"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "setDescription no response")
                result.value = Result.Error(IOException("setDescription2 Error: failure", t))
            }
        })
        return result
    }

    /**
     * Propagates the deleteTag process to the lower level
     *
     * does the POST call and observes the result
     * Sets the Repository's tagDeletedSuccessfully live data with the response of the call
     * @param email key of the user
     * @param tag tag to delete
     * @return mutable live data which will be updated with the result of the call
     */
    fun deleteTag(email: String, tag: String): MutableLiveData<ResponseBody> {
        val call: Call<ResponseBody> = userService!!.deleteTag(email, NomTag(nomTag = tag))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                tagDeletedSuccessfully.value = response.body()
                if (response.isSuccessful) {
                    Log.d("response", "deleteTag response: is successful")
                } else {
                    Log.d("response", "deleteTag response: unsuccessful")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "deleteTag no response")
                tagDeletedSuccessfully.value = ResponseBody.create(null, "no response")
            }
        })
        return tagDeletedSuccessfully
    }

    /**
     * Propagates the addTag process to the lower level
     *
     * does the POST call and observes the result
     * Sets the Repository's tagAddedSuccessfully live data with the response of the call
     * @param email key of the user
     * @param tag tag to add
     * @return mutable live data which will be updated with the result of the call
     */
    fun addTag(email: String, tag: String): MutableLiveData<ResponseBody> {
        val call: Call<ResponseBody> = userService!!.addTag(email, NomTag(nomTag = tag))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                tagAddedSuccessfully.value = response.body()
                if (response.isSuccessful) {
                    Log.d("response", "addTag response: is successful")
                } else {
                    Log.d("response", "addTag response: unsuccessful")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "addTag no response")
                tagAddedSuccessfully.value = ResponseBody.create(null, "no response")
            }
        })
        return tagAddedSuccessfully
    }

    /**
     * obtains profile info of a user from a username
     */
    fun getProfileInfoByUsername(newText: String): Result<MutableLiveData<UserInfo>> {
        if (userInfo == null) userInfo = MutableLiveData<UserInfo>() // linea afegida perque no peti. la he copiat de ActivitiesRepository
        val call: Call<UserInfo> = userService!!.getProfileInfoByUsername(newText)
        call.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    userInfo!!.value = response.body()
                    Log.d("success response", "got a response indicating success")
                } else {
                    Log.d("failure response", "got a response indicating failure")
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("GET", "Error getting topProfileInfo. communication failure (no response)")
            }
        })

        return Result.Success(userInfo!!)
    }

    /**
     * It calls the backend to start following
     * @param currentUser is the logged user
     * @param email is the email of the email the user to follow
     * @returns the MutableLiveData with the response
     */
    fun follow(currentUser: String, email: String): LiveData<String> {
        val result = MutableLiveData<String>()

        val call: Call<ResponseBody> = userService!!.follow(currentUser, FollowUnfollow(email))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.string()
                    Log.d("success response", "follow: got a response indicating success")
                } else {
                    Log.d("failure response", "follow: got a response indicating failure")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("GET", "follow: Error getting info. communication failure (no response)")
            }
        })
        return result
    }

    /**
     * It calls the backend to stop following
     * @param currentUser is the logged user
     * @param email is the email of the email the user to unfollow
     * @returns the MutableLiveData with the response
     */
    fun stopFollowing(currentUser: String, email: String): LiveData<String> {
        val result = MutableLiveData<String>()

        val call: Call<ResponseBody> = userService!!.stopFollowing(currentUser, FollowUnfollow(email))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.string()
                    Log.d("success response", "stopFollowing: got a response indicating success")
                } else {
                    Log.d("failure response", "stopFollowing: got a response indicating failure")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("GET", "stopFollowing: Error getting info. communication failure (no response)")
            }
        })
        return result
    }

    /**
     * It calls the backend to get follows of a user
     * @param currentUser is the user we get the info of
     * @returns the MutableLiveData with the response
     */
    fun following(currentUser: String): LiveData<List<FollowingUser>> {
        val result = MutableLiveData<List<FollowingUser>>()

        val call: Call<List<FollowingUser>> = userService!!.following(currentUser)
        call.enqueue(object : Callback<List<FollowingUser>> {
            override fun onResponse(call: Call<List<FollowingUser>>, response: Response<List<FollowingUser>>) {
                if (response.isSuccessful) {
                    result.value = response.body()
                    Log.d("success response", "following: got a response indicating success")
                } else {
                    Log.d("failure response", "following: got a response indicating failure")
                }
            }

            override fun onFailure(call: Call<List<FollowingUser>>, t: Throwable) {
                Log.d("GET", "following: Error getting info. communication failure (no response)")
            }
        })
        return result
    }

    /**
     * This function handles the action of uploading a photo from the device of the user
     * @param email The email of the user in order to be able to edit his profile
     * @param photoPath The path of the photo desired
     */
    fun uploadPhoto(email: String, photoPath: String?) {
        val file = File(photoPath)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/jpg"), file)
        val call: Call<ResponseBody> = userService!!.uploadProfilePhoto(email, requestBody)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (photoPath != null) {
                        SharedPreferenceManager.setStringValue(Constants().PREF_PHOTO, photoPath)
                    }
                } else {
                    Log.d("GET", "Error uploading the image")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("GET", "Error uploading the image")
            }
        })
    }

    fun getFollowedUsers(email: String): MutableLiveData<List<UserInfo>> {
        if (followedUsers == null) followedUsers = MutableLiveData<List<UserInfo>>()

        val call: Call<List<UserInfo>> = userService!!.getFollowedUsers(email)

        call.enqueue(object : Callback<List<UserInfo>> {
            override fun onResponse(call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                if (response.isSuccessful) {
                    followedUsers!!.value = response.body()
                    Log.d("response", "getFollowedUsers response: is successful")
                } else {
                    Log.d("response", "getFollowedUsers response: unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                Log.d("GET", "Error getting getFollowedUsers. communication failure (no response)")
            }
        })

        return followedUsers as MutableLiveData<List<UserInfo>>
    }

    fun deleteAccount() {
        // delete account
    }
}
