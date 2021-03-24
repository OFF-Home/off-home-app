package com.offhome.app.ui.profile

import androidx.lifecycle.*
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.TopProfileInfo

class ProfileViewModel : ViewModel() {

    private var repository = ProfileRepository()

    private val _topProfileInfo = MutableLiveData<TopProfileInfo>()
    val topProfileInfo: LiveData<TopProfileInfo> = _topProfileInfo

    fun getTopProfileInfo() {
        _topProfileInfo.value = repository.getTopProfileInfo()
    }
}
