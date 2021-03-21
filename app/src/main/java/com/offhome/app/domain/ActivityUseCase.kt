package com.offhome.app.domain

import com.offhome.app.data.ActivityDataSet
import com.offhome.app.model.Activity

class ActivityUseCase {

    val activityDataSet = ActivityDataSet()

    fun getActivityList(): List<Activity>{
        return activityDataSet.createActivitiesList()
    }
}