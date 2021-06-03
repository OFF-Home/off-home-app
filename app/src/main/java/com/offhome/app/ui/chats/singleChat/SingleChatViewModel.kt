package com.offhome.app.ui.chats.singleChat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.SendNotification
import com.offhome.app.data.model.UserInfo

class SingleChatViewModel(val chatRepository: ChatRepository) : ViewModel() {
    val userRepository = ProfileRepository()

    fun getInfoUser(uid: String): MutableLiveData<Result<UserInfo>> {
        return userRepository.getProfileInfoByUID(uid)
    }

    fun sendNotification(not: SendNotification): MutableLiveData<Result<String>> {
        return userRepository.sendNotification(not)
    }

    fun getProfileInfoByUsername(username: String): MutableLiveData<Result<UserInfo>> {
        return userRepository.getProfileInfoByUsername(username)
    }
}
