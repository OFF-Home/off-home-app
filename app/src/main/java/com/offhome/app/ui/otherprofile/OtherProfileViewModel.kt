package com.offhome.app.ui.otherprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo

class OtherProfileViewModel : ViewModel()  {
    /*private var _userInfo = MutableLiveData<UserInfo>()
    var userInfo: LiveData<UserInfo> = _userInfo*/
    private lateinit var userInfo :UserInfo
    lateinit var isFollowing: MutableLiveData<Boolean>
    lateinit var followResult: MutableLiveData<Boolean>
    private var repository = ProfileRepository()

    /*fun getProfileInfo() {
        val username = "victorfer" // stub

     //   userInfo = repository.getProfileInfo(username)!!.value
    }*/

    fun setUserInfo(uinfo:UserInfo) {
        userInfo = uinfo
    }

    fun getUserInfo(): UserInfo {
        return userInfo
    }

    fun isFollowing(): Boolean? {
        val currentUser = "currentUser"
        isFollowing = repository.isFollowing(currentUser, userInfo.email) as MutableLiveData<Boolean>
        return isFollowing.value
    }

    fun follow() {
        val currentUser = "currentUser"
        followResult.value = repository.follow(currentUser, userInfo.email).value == "OK"
        isFollowing.value = true
        userInfo.followers += 1
    }

    fun stopFollowing() {
        val currentUser = "currentUser"
        followResult.value = repository.stopFollowing(currentUser, userInfo.email).value == "OK"
        isFollowing.value = false
        userInfo.followers -= 1
    }
}