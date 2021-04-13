package com.offhome.app.ui.activitieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityFromList

class ActivitiesViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    private lateinit var activitiesList: LiveData<List<ActivityFromList>>

    fun getActivitiesList(categoryName: String): LiveData<List<ActivityFromList>> {
        activitiesList = repository.getAll(categoryName)
        return activitiesList
    }

    fun getNewActivities(categoryName: String): LiveData<List<ActivityFromList>> {
        activitiesList = repository.getAll(categoryName)
        return activitiesList
    }
}