package com.offhome.app.ui.createactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityData

/**
 * View Model for CreateActivity Activity
 * @author Maria Nievas Viñals
 */
class CreateActivityViewModel : ViewModel() {

    private var repository: ActivitiesRepository = ActivitiesRepository()

    fun addActivity(activity: ActivityData): MutableLiveData<String> {
        return repository.addActivity(activity)
    }
}