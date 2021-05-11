package com.offhome.app.ui.chats.singleChat



import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.common.MyApp
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.Result
import com.offhome.app.model.Message

class SingleChatViewModel(val chatRepository: ChatRepository) : ViewModel() {
    var listMessages: MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()
    var sendMessageResult = MutableLiveData<Result<String>>()

    /**
     * It calls the repository to get the messages of a chat
     */
    fun getMessages(uid1: String, uid2: String, activity: AppCompatActivity) {
        (chatRepository.getMessages(uid1, uid2)).observe(
            activity,
            {
                if (it is Result.Success) {
                    listMessages.value = it.data
                } else {
                    Toast.makeText(MyApp.getContext(), MyApp.getContext().getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun sendMessage(uid1: String, uid2: String, text: String) {
        sendMessageResult = chatRepository.sendMessage(uid1, uid2, text)
    }
}
