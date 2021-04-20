package com.offhome.app.ui.profile

import android.text.Editable
import androidx.lifecycle.*
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo

/**
 * Class *ProfileFragmentViewModel*
 *
 * ViewModel for the Profile screen.
 *
 * @author Ferran
 * @property repository reference to the Repository (Model) object
 * @property _profileInfo private mutable live data of the profile info displayed at the top, obtained from the server
 * @property profileInfo public live data for the topProfileInfo
 */
class ProfileFragmentViewModel : ViewModel() {

    private var repository = ProfileRepository()

    private var _profileInfo = MutableLiveData<UserInfo>()
    var profileInfo: LiveData<UserInfo> = _profileInfo

    private var _setUsernameSuccessfully = MutableLiveData<Boolean>()
    var setUsernameSuccessfully: LiveData<Boolean> = _setUsernameSuccessfully

    /**
     * obtains topProfileInfo from the lower level and places it on the live data
     */
    fun getProfileInfo() {
        val username = "victorfer" // stub

        profileInfo = repository.getProfileInfo(username)!!
    }

    fun usernameChangedByUser(newUsername: Editable) {
        val email = "victor@gmai.com" // stub
        setUsernameSuccessfully = repository.setUsername(email, newUsername.toString())!!
    }

    fun descriptionChangedByUser(newDescription: Editable) {
        repository.setDescription("victor@gmai.com", newDescription.toString())
    }
}
