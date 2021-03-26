package com.offhome.app.model.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.retrofit.UserClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        // accés a Backend
        val call: Call<UserInfo> = userService!!.getProfileInfo(username)
        call.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    userInfo?.value = response.body()
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("GET", "Error getting topProfileInfo")
            }
        })

        return userInfo as MutableLiveData<UserInfo>
        // return TopProfileInfo(username = "Maria", starRating = 6) // stub
    }
}
