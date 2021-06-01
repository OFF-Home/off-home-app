package com.offhome.app.ui.activitieslist



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList

/**
 * ViewModel for Activities
 * @property repository is the repository to get the activities
 * @property activitiesList is the list of activities as live data
 */
class ActivitiesViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    private lateinit var activitiesList: MutableLiveData<Result<List<ActivityFromList>>>
    private lateinit var oldActivitiesList: MutableLiveData<Result<List<ActivityFromList>>>
    private lateinit var likedActivitiesList: MutableLiveData<Result<List<ActivityFromList>>>

    /**
     * gets the activities of a category from the repository
     */
    fun getActivitiesList(categoryName: String): MutableLiveData<Result<List<ActivityFromList>>> {
        activitiesList = repository.getAll(categoryName)!!
        return activitiesList
    }

    fun getActivitiesByDescTitle(): MutableLiveData<List<ActivityFromList>> {
        return repository.getActivitiesByDescTitle()
    }

    fun getActivitiesByAscTitle(): MutableLiveData<List<ActivityFromList>> {
        return repository.getActivitiesByAscTitle()
    }

    fun getActivitiesByDate(): MutableLiveData<List<ActivityFromList>> {
        return repository.getActivitiesByDate()
    }

    /**
     * gets the old activities a user has joined from the repository
     */
    fun getOldActivitiesList(userEmail: String): MutableLiveData<Result<List<ActivityFromList>>> {
        oldActivitiesList = repository.getOldAct(userEmail)
        return oldActivitiesList
    }

    /**
     * gets the old activities a user has joined from the repository
     */
    fun getLikedActivitiesList(userEmail: String): MutableLiveData<Result<List<ActivityFromList>>> {
        likedActivitiesList = repository.getLikedAct(userEmail)
        return likedActivitiesList
    }
}
