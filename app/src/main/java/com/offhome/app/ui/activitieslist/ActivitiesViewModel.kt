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
    private lateinit var activitiesList: MutableLiveData<List<ActivityFromList>>
    private lateinit var oldActivitiesList: LiveData<List<ActivityFromList>>
    private lateinit var likedActivitiesList: LiveData<List<ActivityFromList>>

    /**
     * gets the activities of a category from the repository
     */
    fun getActivitiesList(categoryName: String): MutableLiveData<List<ActivityFromList>> {
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

    fun getActivitiesByRadi(
        categoryName: String,
        altitude: Double,
        longitude: Double,
        progress: Int
    ): MutableLiveData<Result<List<ActivityFromList>>> {
        return repository.getActivitiesByRadi(categoryName, altitude, longitude, progress)
    }
}
