package com.offhome.app.ui.activitieslist



import androidx.lifecycle.LiveData
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
    private lateinit var activitiesList: LiveData<List<ActivityFromList>>
    private lateinit var oldActivitiesList: LiveData<List<ActivityFromList>>
    private lateinit var likedActivitiesList: LiveData<List<ActivityFromList>>

    /**
     * gets the activities of a category from the repository
     */
    fun getActivitiesList(categoryName: String): LiveData<List<ActivityFromList>> {
        activitiesList = repository.getAll(categoryName)
        return activitiesList
    }

    /**
     * gets the old activities a user has joined from the repository
     */
    fun getOldActivitiesList(userEmail: String): LiveData<List<ActivityFromList>> {
        oldActivitiesList = repository.getOldAct(userEmail)
        return oldActivitiesList
    }

    /**
     * gets the old activities a user has joined from the repository
     */
    fun getLikedActivitiesList(userEmail: String): LiveData<List<ActivityFromList>> {
        likedActivitiesList = repository.getLikedAct(userEmail)
        return likedActivitiesList
    }
}
