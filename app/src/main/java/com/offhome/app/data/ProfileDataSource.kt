package com.offhome.app.data

import com.offhome.app.model.profile.TopProfileInfo

class ProfileDataSource {

    /*private val _topProfileInfo = MutableLiveData<TopProfileInfo>()
    val topProfileInfo: LiveData<TopProfileInfo> = _topProfileInfo // aquest és observat pel repo*/

    fun getTopProfileInfo(): TopProfileInfo {
        // TODO accés a Backend
        // _topProfileInfo.value = TopProfileInfo(username="Maria", starRating = 6)

        return TopProfileInfo(username = "Maria", starRating = 6) // stub
    }
}
