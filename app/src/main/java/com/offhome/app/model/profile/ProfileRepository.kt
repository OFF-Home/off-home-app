package com.offhome.app.model.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.retrofit.UserClient
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
                    Log.d("response", "response: is successful")

                    setUsernameSuccessfully!!.value = true
                } else { // si rebem resposta de la BD pero ens informa d'un error
                    Log.d("response", "response: unsuccessful")
                    setUsernameSuccessfully!!.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("no response", "no response")
                t.printStackTrace()
                Log.w("Sign-up-back", "createUserWithEmail:failure", t.cause)
                setUsernameSuccessfully!!.value = false
            }
        })
        return setUsernameSuccessfully as MutableLiveData<Boolean>
    }

    fun setDescription(email:String, newDescription:String) {
        //fer quan sapiga fer el setUsername()
    }
}
