package com.offhome.app.ui.profile

import androidx.lifecycle.*
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.TopProfileInfo
import com.offhome.app.model.profile.UserInfo

/**
 * Class *ProfileFragmentViewModel*
 *
 * ViewModel for the Profile screen.
 *
 * @author Ferran
 * @property repository reference to the Repository (Model) object
 * @property _topProfileInfo private mutable live data of the profile info displayed at the top, obtained from the server
 * @property topProfileInfo public live data for the topProfileInfo
 */
class ProfileFragmentViewModel : ViewModel() {

    private var repository = ProfileRepository()

    private var _ProfileInfo = MutableLiveData<UserInfo>()
    val ProfileInfo: LiveData<UserInfo> = _ProfileInfo

    /**
     * obtains topProfileInfo from the lower level and places it on the live data
     */
    fun getProfileInfo() {
        val username = "victorfer" //stub
        _ProfileInfo = repository.getProfileInfo(username)!!
    }
}
