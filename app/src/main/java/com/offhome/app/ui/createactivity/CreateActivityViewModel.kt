package com.offhome.app.ui.createactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.model.ActivityFromList

class CreateActivityViewModel : ViewModel() {

    /*per provar amb la BD
    private var repository: ActivitiesRepository = ActivitiesRepository()
    private var activitiesList: LiveData<List<ActivityFromList>> = repository.getAll()!!
    fun getActivitiesList(): LiveData<List<ActivityFromList>> {
        return activitiesList
    }
    fun getNewActivities(): LiveData<List<ActivityFromList>> {
        activitiesList = repository.getAll()
        return activitiesList
    }*/

   /* init{
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

    fun addActivity(){
    }*/

    private var repository: Repository = Repository()
    private var activitiesList: LiveData<List<ActivityFromList>> = repository.getAllActivities()

    fun getActivitiesList(): LiveData<List<ActivityFromList>> {
        return activitiesList
    }
}