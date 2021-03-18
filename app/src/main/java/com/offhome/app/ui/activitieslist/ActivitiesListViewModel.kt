package com.offhome.app.ui.activitieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.Category

class ActivitiesListViewModel: ViewModel() {
    private var repository: Repository = Repository()
    private var activitiesList: LiveData<List<ActivityFromList>> = repository.getAllActivities()!!

    fun getActivitiesList(): LiveData<List<ActivityFromList>> {
        return activitiesList
    }
}