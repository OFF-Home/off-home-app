package com.offhome.app.ui.chats.listChats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.data.model.UserInfo

/**
 * ViewModel for list of chats
 * @param repository is the repository of the class
 * @property activitiesRepository is the repository for getting the info of the activity
 * @property userRepository is the repository for getting the info of the user
 * @author Pau Cuesta
 */
class ListChatsViewModel(val repository: ChatRepository) : ViewModel() {
    val activitiesRepository = ActivitiesRepository()
    val userRepository = ProfileRepository()

    fun getChats(): MutableLiveData<Result<List<String>>> {
        return repository.getChats(SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString())
    }

    fun getActivityInfo(userCreator: String, dataHoraIni: String): MutableLiveData<Result<ActivityFromList>> {
        return activitiesRepository.getActivityResult(userCreator, dataHoraIni)
    }

    fun getInfoUser(uid: String): MutableLiveData<Result<UserInfo>> {
        return userRepository.getProfileInfoByUID(uid)
    }
}
