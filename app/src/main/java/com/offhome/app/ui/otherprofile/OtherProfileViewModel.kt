package com.offhome.app.ui.otherprofile



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.AchievementData
import com.offhome.app.data.model.FollowingUser
import com.offhome.app.data.model.TagData
import com.offhome.app.data.model.UserInfo

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
    var listFollowing = MutableLiveData<Result<List<FollowingUser>>>()
    var isFollowingValue = MutableLiveData(false)
    var followResult = MutableLiveData<Result<String>>()
    private var repository = ProfileRepository()
    private val currentUser = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()

    var userTagsFromBack = MutableLiveData<Result<List<TagData>>>()

    /**
     * It sets de info to the user
     */
    fun setUserInfo(uinfo: UserInfo) {
        userInfo = uinfo
    }

    fun updateFollowers(num: Int) {
        userInfo.followers += num
    }

    /**
     * It gets the info of the user
     */
    fun getUserInfo(): UserInfo {
        return userInfo
    }

    // els tags van separats de userInfo; per tant no els passen des de l'activitat anterior, ja que cap activitat anterior haurà necessitat els tags.
    // per tant faig GET dels tags de back. Per tant és probable que l'atribut userTags sobri.
    fun setUserTags(tags: List<TagData>) {
    }

    fun getUserTags(): MutableLiveData<Result<List<TagData>>> {
        // return userTags
        return repository.getUserTagsResult(userInfo.email)
    }

    /**
     * It calls the repository to get if one user follows the other
     */
    fun isFollowing(): MutableLiveData<Result<List<FollowingUser>>> {
        listFollowing = repository.following(userInfo.email)
        return listFollowing
    }

    /**
     * It calls the repository to start following a new user
     */
    fun follow(): MutableLiveData<Result<String>> {
        return repository.follow(currentUser, userInfo.email)
    }

    /**
     * It calls the repository to stop following a user
     */
    fun stopFollowing(): MutableLiveData<Result<String>> {
        return repository.stopFollowing(currentUser, userInfo.email)
    }

    /**
     * It sets if it is following or not
     */
    fun setFollowing(b: Boolean) {
        isFollowingValue.value = b
    }

    fun getAchievements(): MutableLiveData<Result<List<AchievementData>>> {
        return repository.getAchievements(userInfo.email)
    }
}
