package com.offhome.app.model.profile

import com.offhome.app.data.ProfileDataSource

class ProfileRepository {

    private var dataSource = ProfileDataSource()

    fun getTopProfileInfo(): TopProfileInfo {
        return dataSource.getTopProfileInfo()
    }
}
