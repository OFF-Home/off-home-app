package com.offhome.app.ui.explore



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.Result
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.model.UserInfo

/**
 * ViewModel of ExploreActivity
 * @property repository references the repository of the Users
 * @property profileInfo is the MutableLiveData for the info of the required user
 * @author Pau Cuesta Arcos
 */
class ExploreViewModel : ViewModel() {
    private var repository = ProfileRepository()
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
}
