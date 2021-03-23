package com.offhome.app.ui.activitieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.model.ActivityFromList

class ActivitiesListViewModel: ViewModel() {
    /*cosas BD si algun dia lo puedo probar
    private var repository: ActivitiesRepository = ActivitiesRepository()
    private var activitiesList: LiveData<List<ActivityFromList>> = repository.getAll()!!

    fun getActivitiesList(): LiveData<List<ActivityFromList>> {
        return activitiesList
    }

    fun getNewActivities(): LiveData<List<ActivityFromList>> {
        activitiesList = repository.getAll()
        return activitiesList
    }*/
    private var repository: Repository = Repository()
    private var activitiesList: LiveData<List<ActivityFromList>> = repository.getAllActivities()!!

    fun getActivitiesList(): LiveData<List<ActivityFromList>> {
        return activitiesList
    }

}