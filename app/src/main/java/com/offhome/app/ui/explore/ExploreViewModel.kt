package com.offhome.app.ui.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.data.Result
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo
import com.offhome.app.ui.login.LoggedInUserView
import com.offhome.app.ui.login.LoginResult

class ExploreViewModel : ViewModel() {
    private var repository = ProfileRepository()
    var profileInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()

    fun searchUser(newText: String) {
        val result: Result<MutableLiveData<UserInfo>> = repository.getProfileInfoByUsername(newText)

        if (result is Result.Success) {
            profileInfo.value = result.data.value
        }
    }
}
