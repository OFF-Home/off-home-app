package com.offhome.app.ui.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.Result
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.data.model.UserInfo

/**
 * ViewModel of ExploreActivity
 * @property repository references the repository of the Users
 * @author Pau Cuesta Arcos
 */
class ExploreViewModel : ViewModel() {
    private var repository = ProfileRepository()
    private var activitiesRepo = ActivitiesRepository()
    private var loggedUserEmail = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()
    var suggestedActivities = MutableLiveData<Result<List<ActivityFromList>>>()
    var friendsActivities = MutableLiveData<Result<List<ActivityFromList>>>()
    var profileInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()

    /**
     * It calls the repository to get the info of the user
     */
    fun searchUser(newText: String): MutableLiveData<Result<UserInfo>> {
        return repository.getProfileInfoByUsername(newText)
    }

    fun getUserInfo(email: String): MutableLiveData<Result<UserInfo>> {
        return repository.getProfileInfo(email)
    }

    /**
     * It calls the repository to get the suggested activities
     */
    fun getSuggestedActivities() {
        suggestedActivities = activitiesRepo.getSuggestedActivities(loggedUserEmail)
    }

    /**
     * It calls the repository to get activities from friends
     */
    fun getFriendsActivities() {
        friendsActivities = activitiesRepo.getFriendsActivities(loggedUserEmail)
    }
}
