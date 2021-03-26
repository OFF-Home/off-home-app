package com.offhome.app.data

/*import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory*/

/**
 * Class *ProfileDataSource*
 *
 * Accesses the server
 *
 * @author Ferran
 */
class ProfileDataSource {

    /*private var retrofit: Retrofit = Retrofit.Builder().baseUrl("http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/").addConverterFactory(GsonConverterFactory.create()).build()
    private var profileDataService: ProfileDataService = retrofit.create(ProfileDataService::class.java)
    var topProfileInfo : MutableLiveData<TopProfileInfo>? = null

    fun getProfileInfo2(username: String): MutableLiveData<TopProfileInfo>? {
        // accés a Backend
        val call: Call<TopProfileInfo> = profileDataService.getProfileInfo(username)
        call.enqueue(object : Callback<TopProfileInfo> {
            override fun onResponse(call: Call<TopProfileInfo>, response: Response<TopProfileInfo>) {
                if (response.isSuccessful) {
                    topProfileInfo?.value = response.body()
                }
            }

            override fun onFailure(call: Call<TopProfileInfo>, t: Throwable) {
                Log.d("GET", "Error getting topProfileInfo")
            }
        })

        return topProfileInfo
        //return TopProfileInfo(username = "Maria", starRating = 6) // stub
    }*/
}
