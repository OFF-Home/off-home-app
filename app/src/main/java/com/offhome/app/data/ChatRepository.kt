package com.offhome.app.data



import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.retrofit.ChatClient
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository(private val chatsClient: ChatClient) {
    private var chatsService = chatsClient.getChatsService()

    fun getChats(userUid: String): MutableLiveData<Result<List<String>>> {
        val result = MutableLiveData<Result<List<String>>>()
        val call = chatsService?.getChats(userUid)
        call!!.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body() as List<String>)
                } else result.value = Result.Error(IOException("Error connecting"))
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                result.value = Result.Error(IOException("Error connecting", t))
            }
        })
        return result
    }
}
