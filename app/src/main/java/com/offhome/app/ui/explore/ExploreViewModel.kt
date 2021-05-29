package com.offhome.app.ui.explore



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.Result
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.model.UserInfo

/**
 * ViewModel of ExploreActivity
 * @property repository references the repository of the Users
 * @author Pau Cuesta Arcos
 */
class ExploreViewModel : ViewModel() {
    private var repository = ProfileRepository()

    /**
     * It calls the repository to get the info of the user
     */
    fun searchUser(newText: String): MutableLiveData<Result<UserInfo>> {
        return repository.getProfileInfoByUsername(newText)
    }
}
