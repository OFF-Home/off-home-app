package com.offhome.app.ui.activitieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityFromList

class ActivitiesListViewModel: ViewModel() {
    /*cosas BD ya funcionan*/
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
    /* old stuff
    private var repository: Repository = Repository()
    private var activitiesList: LiveData<List<ActivityFromList>> = repository.getAllActivities()!!

    fun getActivitiesList(): LiveData<List<ActivityFromList>> {
        return activitiesList
    }*/

}