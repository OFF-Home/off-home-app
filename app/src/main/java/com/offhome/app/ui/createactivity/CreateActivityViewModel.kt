package com.offhome.app.ui.createactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.ActivityData


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

*/

    private var repository: ActivitiesRepository = ActivitiesRepository()

    fun addActivity(activity: ActivityData): MutableLiveData<String> {
        return repository.addActivity(activity)
    }

}