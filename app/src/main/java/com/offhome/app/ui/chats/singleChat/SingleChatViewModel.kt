package com.offhome.app.ui.chats.singleChat



import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.common.MyApp
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.Message

class SingleChatViewModel(val chatRepository: ChatRepository) : ViewModel() {
    var listMessages: MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()
    var sendMessageResult = MutableLiveData<Result<String>>()
}
