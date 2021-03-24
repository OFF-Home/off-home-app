package com.offhome.app.ui.profile

import androidx.lifecycle.*
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.TopProfileInfo

class ProfileViewModel : ViewModel() {

    private var repository = ProfileRepository()

    private val _topProfileInfo = MutableLiveData<TopProfileInfo>()
    val topProfileInfo: LiveData<TopProfileInfo> = _topProfileInfo // TODO aquest és observat pel fragment.

    fun getTopProfileInfo(/*viewLifecycleOwner : LifecycleOwner*/) {

        // return repository.getTopProfileInfo()
       /*repository.topProfileInfo.observe(viewLifecycleOwner, Observer {
           val topProfileInfoRepo = it ?: return@Observer

           _topProfileInfo.value = topProfileInfoRepo
       })*/

        _topProfileInfo.value = repository.getTopProfileInfo()
    }
}
