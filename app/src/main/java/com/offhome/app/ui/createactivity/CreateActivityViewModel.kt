package com.offhome.app.ui.createactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.domain.ActivityUseCase
import com.offhome.app.model.Activity

class CreateActivityViewModel : ViewModel() {
    private val repository: Repository = Repository()

    val activityUseCase = ActivityUseCase()
    private val activitylistData = MutableLiveData<List<Activity>>()

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is CreationActivity Activity"
    }*/

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


}