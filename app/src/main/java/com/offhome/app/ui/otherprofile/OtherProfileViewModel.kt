package com.offhome.app.ui.otherprofile

import androidx.lifecycle.ViewModel
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo

class OtherProfileViewModel : ViewModel() {
    /*private var _userInfo = MutableLiveData<UserInfo>()
    var userInfo: LiveData<UserInfo> = _userInfo*/
    private lateinit var userInfo: UserInfo
    private var repository = ProfileRepository()

    /*fun getProfileInfo() {
        val username = "victorfer" // stub

     //   userInfo = repository.getProfileInfo(username)!!.value
    }*/

    fun setUserInfo(uinfo: UserInfo) {
        userInfo = uinfo
    }
    fun getUserInfo(): UserInfo {
        return userInfo
    }


    fun uploadPhoto(photoPath: String){
        repository.uploadPhoto(photoPath);
    }
}
