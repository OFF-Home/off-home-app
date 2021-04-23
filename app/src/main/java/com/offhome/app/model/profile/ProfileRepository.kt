package com.offhome.app.model.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.retrofit.UserClient
import com.offhome.app.model.ActivityFromList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream

/**
 * Class *ProfileRepository*
 *
 * Repository for the Profile screen. Plays the "Model" role in this screen's MVVM
 * Encapsulates the access to data from the ViewModel
 *
 * @author Ferran
 * @property dataSource reference to the DataSource object
 */
class ProfileRepository {

    private val userClient = UserClient()
    private var userService = userClient.getUserService()
    var userInfo: MutableLiveData<UserInfo>? = null
    var setUsernameSuccessfully: MutableLiveData<Boolean>? = null
    var setDescriptionSuccessfully: MutableLiveData<Boolean>? = null
    var activities: MutableLiveData<List<ActivityFromList>>?=null
    var tags: MutableLiveData< List<TagData> >?=null

    /**
     * obtains topProfileInfo from the lower level and returns it   //TODO
     */
    fun getProfileInfo(username: String): MutableLiveData<UserInfo>? {

        if (userInfo == null) userInfo = MutableLiveData<UserInfo>() // linea afegida perque no peti. la he copiat de ActivitiesRepository

        // accés a Backend
        val call: Call<UserInfo> = userService!!.getProfileInfo(username)
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

        // stub:
       /* userInfo?.value = UserInfo(email="yesThisIsVictor@gmail.com", username = "victorfer", password = "1234", birthDate = "12-12-12",
            description = "Lou Spence (1917–1950) was a fighter pilot and squadron commander in the Royal Australian Air Force during World War II and the Korean War. In 1941 he was posted to North Africa with No. 3 Squadron, which operated P-40 Tomahawks and Kittyhawks; he was credited with shooting down two German aircraft and earned the Distinguished Flying Cross (DFC). He commanded No. 452 Squadron in ",
            followers = 200, following = 90, darkmode = 0, notifications = 0, estrelles = 3, tags="a b c d e", language = "esp")
*/

        return userInfo as MutableLiveData<UserInfo>
        // return TopProfileInfo(username = "Maria", starRating = 6) // stub
    }

    fun getUserActivities(email: String): MutableLiveData<List<ActivityFromList>>? {
        if (activities == null) activities = MutableLiveData<List<ActivityFromList>>()

        val call: Call<List<ActivityFromList>> = userService!!.getUserActivities(email)
        call.enqueue(object : Callback<List<ActivityFromList>> {
            override fun onResponse(call: Call<List<ActivityFromList>>, response: Response<List<ActivityFromList>>) {
                if (response.isSuccessful) {
                    activities!!.value =response.body()
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

    fun getUserTags(email: String): MutableLiveData<List<TagData>>? {       //dona failure. potser el tipus no és el q toca
        if (tags == null) tags = MutableLiveData< List<TagData> >()

        val call: Call<List<TagData>> = userService!!.getTags(/*email*/ username = "victor@gmai.com")
        call.enqueue(object : Callback< List<TagData> > {
            override fun onResponse(call: Call< List<TagData> >, response: Response< List<TagData> >) {
                if (response.isSuccessful) {
                    tags!!.value =response.body()
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

    // per quan agafem la profilePic de backend
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

    fun setUsername(email:String, newUsername: String): MutableLiveData<Boolean>? {    //email identifica a l'user
        //in progress
        if (setUsernameSuccessfully == null) setUsernameSuccessfully = MutableLiveData<Boolean>() // linea afegida perque no peti.

        val call: Call<ResponseBody> = userService!!.setUsername(email = email, username = newUsername)   //o algo tipo updateUser()        //he posat "!!"

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("response", "setUsername response: is successful")

                    setUsernameSuccessfully!!.value = true
                } else { // si rebem resposta de la BD pero ens informa d'un error
                    Log.d("response", "setUsername response: unsuccessful")
                    setUsernameSuccessfully!!.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "setUsername no response")
                t.printStackTrace()
                Log.w("no response", "setUsername no response", t.cause)
                setUsernameSuccessfully!!.value = false
            }
        })

        return setUsernameSuccessfully as MutableLiveData<Boolean>
    }

    fun setDescription(email:String, newDescription:String): MutableLiveData<Boolean>? {
        //basicament igual que setUsername. si arreglo una, fer copia-pega
        if (setDescriptionSuccessfully == null) setDescriptionSuccessfully = MutableLiveData<Boolean>()
        val call: Call<ResponseBody> = userService!!.setDescription(email = email, description = newDescription)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("response", "setDescription response: is successful")
                    setDescriptionSuccessfully!!.value = true
                } else {
                    Log.d("response", "setDescription response: unsuccessful")
                    setDescriptionSuccessfully!!.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "setDescription no response")
                t.printStackTrace()
                Log.w("no response", "setDescription no response", t.cause)
                setDescriptionSuccessfully!!.value = false
            }
        })
        return setDescriptionSuccessfully as MutableLiveData<Boolean>
    }

    fun deleteTag(email:String, tag:String){
    }

    fun addTag(email:String, tag:String) {
        //TODO falta el livedata de response.

        val call: Call<ResponseBody> = userService!!.addTag(email, tag)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("response", "addTag response: is successful")
                } else {
                    Log.d("response", "addTag response: unsuccessful")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "addTag no response")

            }
        })
    }
}
