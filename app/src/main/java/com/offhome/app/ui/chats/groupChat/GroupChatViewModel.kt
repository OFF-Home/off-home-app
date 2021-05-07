package com.offhome.app.ui.chats.groupChat

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.common.MyApp
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.Result
import com.offhome.app.model.Message

class GroupChatViewModel: ViewModel() {
    private var repository = ChatRepository()
    var listMessages: MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()

    /**
     * It calls the repository to get the messages of a group chat
     */
    fun getMessages(uid_creator: String, data_hora_ini: String) {
        val result = repository.getMessagesGroup(uid_creator, data_hora_ini)
        if (result is Result.Success) {
            listMessages = result.data as MutableLiveData<List<Message>>
        } else {
            Toast.makeText(MyApp.getContext(), MyApp.getContext().getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMessage(text: String) {
        repository.sendMessage(text)
    }
}
