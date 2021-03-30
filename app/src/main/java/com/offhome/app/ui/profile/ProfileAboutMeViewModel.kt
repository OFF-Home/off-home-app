package com.offhome.app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo

class ProfileAboutMeViewModel : ViewModel() {
    //private var profileRepository = ProfileRepository() //agafar el que ja existeix. he fet el de profile pq ell ja té les dades que volem

    private var _ProfileInfo = MutableLiveData<UserInfo>()
    val ProfileInfo: LiveData<UserInfo> = _ProfileInfo

    /*fun getProfileInfo() {
        val username = "victorfer" // stub
        _ProfileInfo = profileRepository.getProfileInfo(username)!!
    }*/
}
