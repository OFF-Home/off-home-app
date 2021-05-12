package com.offhome.app.ui.activitieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityFromList

/**
 * ViewModel for Activities
 * @property repository is the repository to get the activities
 * @property activitiesList is the list of activities as live data
 */
class ActivitiesViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    private lateinit var activitiesList: MutableLiveData<List<ActivityFromList>>

    /**
     * gets the activities from the repository
     */
    fun getActivitiesList(categoryName: String): MutableLiveData<List<ActivityFromList>> {
        activitiesList = repository.getAll(categoryName)!!
        return activitiesList
    }
}
