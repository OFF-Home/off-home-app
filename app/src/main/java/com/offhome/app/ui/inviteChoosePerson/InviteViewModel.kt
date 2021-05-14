package com.offhome.app.ui.inviteChoosePerson



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserSummaryInfo

class InviteViewModel : ViewModel() {
    private var profileRepository: ProfileRepository = ProfileRepository()
    private var loggedUserEmail = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()

    private var _followedUsers = MutableLiveData<List<UserSummaryInfo>>()
    var followedUsers: LiveData<List<UserSummaryInfo>> = _followedUsers

    private var nSelectedRecipients: Int = 0

    fun getFollowedUsers() {
        // followedUsers = profileRepository.getFollowedUsers(loggedUserEmail)
    }
}
