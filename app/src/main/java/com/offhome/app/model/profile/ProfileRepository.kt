package com.offhome.app.model.profile

import com.offhome.app.data.ProfileDataSource

class ProfileRepository {

    private var dataSource = ProfileDataSource()

    /*private val _topProfileInfo = MutableLiveData<TopProfileInfo>()
    val topProfileInfo: LiveData<TopProfileInfo> = _topProfileInfo // aquest és observat pel VM*/

    fun getTopProfileInfo(): TopProfileInfo {
        // dataSource.topProfileInfo.observe()
        return dataSource.getTopProfileInfo()
    }
}
