package com.offhome.app.ui.inviteChoosePerson



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo

class InviteViewModel : ViewModel() {
    private var profileRepository: ProfileRepository = ProfileRepository()
    private var loggedUserEmail = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()

    private var _followedUsers = MutableLiveData<List<UserInfo>>()
    var followedUsers: LiveData<List<UserInfo>> = _followedUsers

    private var _allUsers = MutableLiveData<List<UserInfo>>()
    var allUsers: LiveData<List<UserInfo>> = _allUsers

    /*private var _participants = MutableLiveData<List<UserInfo>>()
    var participants: LiveData<List<UserInfo>> =_participants*/

    private var currentUID: String = SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString()

    private var nSelectedRecipients: Int = 0

    fun getFollowedUsers() {
        followedUsers = profileRepository.getFollowedUsers(loggedUserEmail)
    }

    fun getAllUsers() {
        // allUsers = profileRepository.getAllUsers()
    }
    fun getCurrentUID(): String = currentUID

    /*fun getParticipants() {
        participants = activitiesRepository.getParticipants
    }*/
}
