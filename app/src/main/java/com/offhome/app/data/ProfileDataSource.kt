package com.offhome.app.data

import com.offhome.app.model.profile.TopProfileInfo

class ProfileDataSource {

    fun getTopProfileInfo(): TopProfileInfo {
        // TODO accés a Backend

        return TopProfileInfo(username = "Maria", starRating = 6) // stub
    }
}
