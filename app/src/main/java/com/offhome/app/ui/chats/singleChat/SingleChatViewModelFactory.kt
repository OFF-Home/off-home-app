package com.offhome.app.ui.chats.singleChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.LoginDataSource
import com.offhome.app.data.LoginRepository
import com.offhome.app.data.retrofit.ChatClient
import com.offhome.app.ui.login.LoginViewModel

class SingleChatViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleChatViewModel::class.java)) {
            return SingleChatViewModel(
                chatRepository = ChatRepository(
                    chatsClient = ChatClient()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
