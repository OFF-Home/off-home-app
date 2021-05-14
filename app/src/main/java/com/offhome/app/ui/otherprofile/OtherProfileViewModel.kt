package com.offhome.app.ui.otherprofile



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.model.FollowingUser
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.TagData
import com.offhome.app.model.profile.UserInfo

/**
 * Class *OtherProfileViewModel*
 *
 * ViewModel for the entire OtherProfile screen. (includes the activity and the aboutThem framgent)
 *
 * @author Pau
 * @property repository reference to the Repository (Model) object
 * @property userInfo user's info
 * @property userTags TODO
 * @property listFollowing TODO
 * @property isFollowing
 * @property followResult
 */
class OtherProfileViewModel : ViewModel() {
    private lateinit var userInfo: UserInfo
    private lateinit var userTags: List<TagData>
    var listFollowing: MutableLiveData<List<FollowingUser>> = MutableLiveData()
    var isFollowing: MutableLiveData<Boolean> = MutableLiveData(false)
    var followResult: MutableLiveData<String> = MutableLiveData()
    private var repository = ProfileRepository()
    private val currentUser = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()

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

    // cal decidir si ajuntarem els tags a userInfo o no.
    fun setUserTags(tags: List<TagData>) {
        userTags = tags
    }
    fun getUserTags(): List<TagData> {
        return userTags
    }

    /**
     * It calls the repository to get if one user follows the other
     */
    fun isFollowing(): List<FollowingUser>? {
        listFollowing = repository.following(userInfo.email) as MutableLiveData<List<FollowingUser>>
        return listFollowing.value
    }

    /**
     * It calls the repository to start following a new user
     */
    fun follow() {
        followResult.value = repository.follow(currentUser, userInfo.email).value
        isFollowing.value = true
        userInfo.followers += 1
    }

    /**
     * It calls the repository to stop following a user
     */
    fun stopFollowing() {
        followResult.value = repository.stopFollowing(currentUser, userInfo.email).value
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
