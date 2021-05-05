package com.offhome.app.data

import androidx.lifecycle.MutableLiveData
import com.offhome.app.model.Message
import java.util.*

class ChatRepository {
    fun getMessages(uid1: String, uid2: String): MutableLiveData<List<Message>> {
        return MutableLiveData(arrayListOf((Message("gter", "Hello", Date(10)))))
    }

    fun sendMessage(text: String) {

    }
}
