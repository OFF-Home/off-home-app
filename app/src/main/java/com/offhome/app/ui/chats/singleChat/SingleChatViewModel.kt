package com.offhome.app.ui.chats.singleChat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ChatRepository
import com.offhome.app.model.Message

class SingleChatViewModel : ViewModel() {
    private var repository = ChatRepository()
    var listMessages: MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()

    /**
     * It calls the repository to get the messages of a chat
     */
    fun getMessages(uid1: String, uid2: String) {
        listMessages = repository.getMessages(uid1, uid2)
    }

    fun sendMessage(text: String) {
        repository.sendMessage(text)
    }
}
