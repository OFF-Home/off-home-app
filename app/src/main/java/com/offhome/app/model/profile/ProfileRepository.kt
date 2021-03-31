package com.offhome.app.model.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.retrofit.UserClient
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

    /**
     * obtains topProfileInfo from the lower level and returns it   //TODO
     */
    fun getProfileInfo(username: String): MutableLiveData<UserInfo>? {
        if (userInfo ==null) userInfo = MutableLiveData<UserInfo>() //linea afegida perque no peti. la he copiat de ActivitiesRepository

        // accés a Backend
        //comentat per testejar coses amb stub
        val call: Call<UserInfo> = userService!!.getProfileInfo(username)
        call.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    userInfo!!.value = response.body()
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("GET", "Error getting topProfileInfo")
            }
        })

        //stub:
       /* userInfo?.value = UserInfo(email="yesThisIsVictor@gmail.com", username = "victorfer", password = "1234", birthDate = "12-12-12",
            description = "Lou Spence (1917–1950) was a fighter pilot and squadron commander in the Royal Australian Air Force during World War II and the Korean War. In 1941 he was posted to North Africa with No. 3 Squadron, which operated P-40 Tomahawks and Kittyhawks; he was credited with shooting down two German aircraft and earned the Distinguished Flying Cross (DFC). He commanded No. 452 Squadron in ",
            followers = 200, following = 90, darkmode = 0, notifications = 0, estrelles = 3, tags="a b c d e", language = "esp")
*/
        return userInfo as MutableLiveData<UserInfo>
        // return TopProfileInfo(username = "Maria", starRating = 6) // stub
    }

    //per quan agafem la profilePic de backend
    private fun getBitmapFromURL(url1:String):Bitmap? {
        var bitmap : Bitmap? = null
        try {
            val inputStream: InputStream = java.net.URL(url1).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        }
        catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }
}
