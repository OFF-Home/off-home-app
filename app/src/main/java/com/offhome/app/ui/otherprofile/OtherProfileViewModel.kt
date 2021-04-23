package com.offhome.app.ui.otherprofile


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.model.FollowingUser
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo

class OtherProfileViewModel : ViewModel() {
    /*private var _userInfo = MutableLiveData<UserInfo>()
    var userInfo: LiveData<UserInfo> = _userInfo*/

    private lateinit var userInfo :UserInfo
    lateinit var listFollowing: MutableLiveData<List<FollowingUser>>
    lateinit var isFollowing: MutableLiveData<Boolean>
    lateinit var followResult: MutableLiveData<Boolean>
    private var repository = ProfileRepository()

    /*fun getProfileInfo() {
        val username = "victorfer" // stub

     //   userInfo = repository.getProfileInfo(username)!!.value
    }*/

    /**
     * It sets de info to the user
     */
    fun setUserInfo(uinfo: UserInfo) {
        userInfo = uinfo
    }

    /**
     * It gets the info of the user
     */
    fun getUserInfo(): UserInfo {
        return userInfo
    }


    fun uploadPhoto(photoPath: String) {
        repository.uploadPhoto(photoPath);
    }

    /**
     * It calls the repository to get if one user follows the other
     */
    fun isFollowing(): List<FollowingUser>? {
        val currentUser = "currentUser"
        listFollowing = repository.following(currentUser) as MutableLiveData<List<FollowingUser>>
        return listFollowing.value
    }

    /**
     * It calls the repository to start following a new user
     */
    fun follow() {
        val currentUser = "currentUser"
        followResult.value = repository.follow(currentUser, userInfo.email).value == "OK"
        isFollowing.value = true
        userInfo.followers += 1
    }

    /**
     * It calls the repository to stop following a user
     */
    fun stopFollowing() {
        val currentUser = "currentUser"
        followResult.value = repository.stopFollowing(currentUser, userInfo.email).value == "OK"
        isFollowing.value = false
        userInfo.followers -= 1
    }

    /**
     * It sets if it is following or not
     */
    fun setFollowing(b: Boolean) {
        isFollowing.value = b
    }
}
