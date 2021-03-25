package com.offhome.app.ui.createactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.model.ActivityFromList


/**
 * Create Activity View Model (not fully implemented yet)
 */
class CreateActivityViewModel : ViewModel() {
   /*
    init{
        getActivitiesList()
    }

    fun setListData(activitylist:List<Activity>){
        activitylistData.value = activitylist
    }

    fun getActivitiesList() {
        setListData(activityUseCase.getActivityList())
    }

    fun getActivitiesListLiveData(): LiveData<List<Activity>>{
        return activitylistData
    }

    fun addActivity(activity: Activity){
    }*/

    private var repository: Repository = Repository()
    private var activitiesList: LiveData<List<ActivityFromList>> = repository.getAllActivities()

    fun getActivitiesList(): LiveData<List<ActivityFromList>> {
        return activitiesList
    }
}