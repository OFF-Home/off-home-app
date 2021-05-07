package com.offhome.app.data



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.model.ChatGroupIdentification
import com.offhome.app.data.model.ChatIndividualIdentification
import com.offhome.app.data.retrofit.ChatClient
import com.offhome.app.model.Message
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository {

    private val chatsClient = ChatClient()
    private var chatsService = chatsClient.getChatsService()

    fun getMessages(uid1: String, uid2: String): Result<LiveData<List<Message>>> {
        lateinit var result: Result<LiveData<List<Message>>>
        val call: Call<List<Message>> = chatsService!!.getAllMessages(
            ChatIndividualIdentification(uid1, uid2)
        )
        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    result = Result.Success(MutableLiveData(response.body()))
                } else {
                    result = Result.Error(IOException("Error connecting"))
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                result = Result.Error(IOException("Error connecting", t))
            }
        })
        return result
    }

    fun getMessagesGroup(uid_creator: String, data_hora_ini: String): Result<LiveData<List<Message>>> {
        lateinit var result: Result<LiveData<List<Message>>>
        val call: Call<List<Message>> = chatsService!!.getAllMessagesGroup(
            ChatGroupIdentification(uid_creator, data_hora_ini)
        )
        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    result = Result.Success(MutableLiveData(response.body()))
                } else {
                    result = Result.Error(IOException("Error connecting"))
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                result = Result.Error(IOException("Error connecting", t))
            }
        })
        return result
    }

    fun sendMessage(text: String) {
    }
}
