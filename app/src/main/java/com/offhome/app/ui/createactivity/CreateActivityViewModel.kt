package com.offhome.app.ui.createactivity



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityData

/**
 * View Model for CreateActivity Activity
 * @author Maria Nievas Viñals
 */
class CreateActivityViewModel : ViewModel() {

    private var repository: ActivitiesRepository = ActivitiesRepository()

    // private var repositoryChat: ChatRepository = ChatRepository()

    fun addActivity(activity: ActivityData): MutableLiveData<Result<String>> {
        return repository.addActivity(activity)
    }

    // fun addChatGroup(chatGroupide: ChatGroupIdentification): MutableLiveData<String> {
    //   return repositoryChat.addChatGroup(chatGroupide)
    // }
}
