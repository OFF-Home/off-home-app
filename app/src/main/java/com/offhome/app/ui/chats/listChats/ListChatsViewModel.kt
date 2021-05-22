package com.offhome.app.ui.chats.listChats

import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.MyApp
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.Result
import com.offhome.app.model.ChatInfo

class ListChatsViewModel(val repository: ChatRepository) : ViewModel() {
    private lateinit var chatList: MutableLiveData<List<ChatInfo>>

    fun getChats(viewLifecycleOwner: LifecycleOwner): LiveData<List<ChatInfo>> {
        repository.getChats(SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString()).observe(viewLifecycleOwner, {
            if (it is Result.Success) {
                chatList.value = it.data
            } else {
                Toast.makeText(MyApp.getContext(), MyApp.getContext().getString(R.string.error_getting_chats), Toast.LENGTH_LONG).show()
            }
        })
        return chatList
    }
}
