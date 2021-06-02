package com.offhome.app.ui.chats.singleChat



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.data.ChatRepository
import com.offhome.app.data.retrofit.ChatClient
import com.offhome.app.ui.chats.groupChat.GroupChatViewModel
import com.offhome.app.ui.chats.listChats.ListChatsViewModel

class SingleChatViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleChatViewModel::class.java)) {
            return SingleChatViewModel(
                chatRepository = ChatRepository(
                    chatsClient = ChatClient()
                )
            ) as T
        } else if (modelClass.isAssignableFrom(GroupChatViewModel::class.java)) {
            return GroupChatViewModel(
                chatRepo = ChatRepository(
                    chatsClient = ChatClient()
                )
            ) as T
        } else if (modelClass.isAssignableFrom(ListChatsViewModel::class.java)) {
            return ListChatsViewModel(
                repository = ChatRepository(
                    chatsClient = ChatClient()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
