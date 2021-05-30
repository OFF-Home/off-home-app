package com.offhome.app.ui.chats.listChats

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.Result
import com.offhome.app.model.ChatInfo

class ListChatsViewModel(val repository: ChatRepository) : ViewModel() {

    fun getChats(viewLifecycleOwner: LifecycleOwner): MutableLiveData<Result<List<ChatInfo>>> {
        return repository.getChats(SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString())
    }
}
