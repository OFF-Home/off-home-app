package com.offhome.app

import androidx.lifecycle.MutableLiveData
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.Category

class Repository {

    var categories: MutableLiveData<List<Category>>? = null
    var activitiesList: MutableLiveData<List<ActivityFromList>>? = null

    fun getAll(): MutableLiveData<List<Category>> {
        val category = Category("Sport", "url", "url")
        if (categories == null) {
            categories = MutableLiveData<List<Category>>(listOf(category, category, category, category, category, category, category, category, category, category, category, category, category, category, category))
        }
        return categories as MutableLiveData<List<Category>>
    }

    fun getAllActivities(): MutableLiveData<List<ActivityFromList>> {
        val activityFromList = ActivityFromList("Running in La Barceloneta", "17/03/2021", "8", "url")
        if (activitiesList == null) {
            activitiesList = MutableLiveData<List<ActivityFromList>>(listOf(activityFromList, activityFromList, activityFromList, activityFromList, activityFromList))
        }
        return activitiesList as MutableLiveData<List<ActivityFromList>>
    }
}
