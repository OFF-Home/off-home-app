package com.offhome.app.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.Result
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo
import com.offhome.app.ui.login.LoggedInUserView
import com.offhome.app.ui.login.LoginResult

/**
 * ViewModel of ExploreActivity
 * @property repository references the repository of the Users
 * @property profileInfo is the MutableLiveData for the info of the required user
 * @author Pau Cuesta Arcos
 */
class ExploreViewModel : ViewModel() {
    private var repository = ProfileRepository()
    private var activitiesRepo = ActivitiesRepository()
    private var loggedUserEmail = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()
    private var _suggestedActivities = MutableLiveData<List<ActivityFromList>>()
    var suggestedActivities: LiveData<List<ActivityFromList>> = _suggestedActivities
    var profileInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()

    /**
     * It calls the repository to get the info of the user
     */
    fun searchUser(newText: String) {
        val result: Result<MutableLiveData<UserInfo>> = repository.getProfileInfoByUsername(newText)

        if (result is Result.Success) {
            profileInfo.value = result.data.value
        }
    }

    fun getSuggestedActivities() {
        suggestedActivities = activitiesRepo.getSuggestedActivities(loggedUserEmail)!!
    }
}
