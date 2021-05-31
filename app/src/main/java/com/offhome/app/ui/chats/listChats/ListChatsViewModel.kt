package com.offhome.app.ui.chats.listChats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.Result
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.UserInfo

class ListChatsViewModel(val repository: ChatRepository) : ViewModel() {
    val activitiesRepository = ActivitiesRepository()
    val userRepository = ProfileRepository()

    fun getChats(): MutableLiveData<Result<List<String>>> {
        return repository.getChats(SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString())
    }

    fun getActivityInfo(userCreator: String, dataHoraIni: String) {
        return activitiesRepository
    }

    fun getInfoUser(uid: String): MutableLiveData<Result<UserInfo>> {
        return userRepository.getProfileInfoByUID(uid)
    }
}
