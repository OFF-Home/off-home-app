package com.offhome.app.ui.profile

import androidx.lifecycle.*
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.TopProfileInfo

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

    private var _topProfileInfo = MutableLiveData<TopProfileInfo>()
    val topProfileInfo: LiveData<TopProfileInfo> = _topProfileInfo

    /**
     * obtains topProfileInfo from the lower level and places it on the live data
     */
    fun getTopProfileInfo() {
        // _topProfileInfo = repository.getTopProfileInfo()!!
    }
}
