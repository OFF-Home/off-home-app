package com.offhome.app.data



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.model.ChatGroupIdentification
import com.offhome.app.data.model.ChatIndividualIdentification
import com.offhome.app.data.retrofit.ChatClient
import com.offhome.app.model.GroupMessage
import com.offhome.app.model.Message
import okhttp3.ResponseBody
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository {

    private val chatsClient = ChatClient()
    private var chatsService = chatsClient.getChatsService()
    private var responseSendMessage: MutableLiveData<String>? = MutableLiveData(" ")

    fun getMessages(uid1: String, uid2: String): MutableLiveData<Result<List<Message>>> {
        val result = MutableLiveData<Result<List<Message>>>()
        val call: Call<List<Message>> = chatsService!!.getAllMessages(
            ChatIndividualIdentification(uid1, uid2)
        )
        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error(IOException("Error connecting"))
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                result.value = Result.Error(IOException("Error connecting", t))
            }
        })
        return result
    }

    fun getMessagesGroup(uid_creator: String, data_hora_ini: String): MutableLiveData<Result<List<Message>>> {
        val result = MutableLiveData<Result<List<Message>>>()
        val call: Call<List<Message>> = chatsService!!.getAllMessagesGroup(
            ChatGroupIdentification(uid_creator, data_hora_ini)
        )
        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error(IOException("Error connecting"))
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                result.value = Result.Error(IOException("Error connecting", t))
            }
        })
        return result
    }

    fun sendMessage(text: String) {
    }


    fun sendGroupMessage(message: GroupMessage): MutableLiveData<String> {
        val call = chatsService?.sendGroupMissage(message)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseSendMessage?.value = "Message send!"
                } else responseSendMessage?.value =
                    "It has been an error and the message cannot be send"
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseSendMessage?.value =
                    "It has been an error and the message cannot be send"
            }
        })
        return responseSendMessage as MutableLiveData<String>
    }

    fun addChatGroup(chatGroupIde: ChatGroupIdentification): MutableLiveData<String>{
        val call = chatsService?.addChatGroup(chatGroupIde)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    responseSendMessage?.value = "Chat group created!"
                } else responseSendMessage?.value =
                    "It has been an error and the chat cannot be created"
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseSendMessage?.value =
                    "It has been an error and the chat cannot be created"
            }
        })
        return responseSendMessage as MutableLiveData<String>
    }

}
