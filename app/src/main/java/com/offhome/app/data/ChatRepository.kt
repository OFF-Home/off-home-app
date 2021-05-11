package com.offhome.app.data



import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.model.ChatGroupIdentification
import com.offhome.app.data.model.SendMessage
import com.offhome.app.data.retrofit.ChatClient
import com.offhome.app.model.GroupMessage
import com.offhome.app.model.Message
import java.io.IOException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository (private val chatsClient: ChatClient) {
    private var chatsService = chatsClient.getChatsService()
    private var responseSendMessage: MutableLiveData<String>? = MutableLiveData(" ")

    fun getMessages(uid1: String, uid2: String): MutableLiveData<Result<List<Message>>> {
        val result = MutableLiveData<Result<List<Message>>>()
        val call: Call<List<Message>> = chatsService!!.getAllMessages(
            uid1, uid2
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
                result.value = Result.Error(IOException("Error connecting to server", t))
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

    fun sendMessage(uid1: String, uid2: String, text: String): MutableLiveData<Result<String>> {
        val result = MutableLiveData<Result<String>>()
        val call = chatsService?.sendMessage(SendMessage(uid1, uid2, uid1, text))
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body().toString())
                } else {
                    result.value = Result.Error(IOException("Error connecting"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result.value = Result.Error(IOException("Error connecting", t))
            }
        })
        return result
    }

    fun sendGroupMessage(message: GroupMessage): MutableLiveData<Result<String>> {
        val result = MutableLiveData<Result<String>>()
        val call = chatsService?.sendGroupMissage(message)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body().toString())
                } else result.value = Result.Error(IOException("Error connecting"))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result.value = Result.Error(IOException("Error connecting", t))
            }
        })
        return result
    }
}
