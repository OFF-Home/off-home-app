package com.offhome.app.data



import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.offhome.app.common.Constants
import com.offhome.app.data.model.ChatGroupIdentification
import com.offhome.app.data.model.ChatIndividualIdentification
import com.offhome.app.data.model.SendMessage
import com.offhome.app.data.retrofit.ChatClient
import com.offhome.app.model.GroupMessage
import com.offhome.app.model.Message
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.io.IOException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository(private val chatsClient: ChatClient) {
    private var chatsService = chatsClient.getChatsService()
    private var responseSendMessage: MutableLiveData<String>? = MutableLiveData(" ")
    var listMessages: MutableLiveData<ArrayList<Message>> = MutableLiveData<ArrayList<Message>>()
    lateinit var mSocket: Socket
    lateinit var userUid: String
    val gson: Gson = Gson()

    fun initializeChatSocket(uid2: String) {
        mSocket.connect()
        userUid = uid2
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("newUserToChatRoom", onNewUser) // To know if the new user entered the room.
        mSocket.on("updateChat", onUpdateChat) // To update if someone send a message to chatroom
        mSocket.on("userLeftChatRoom", onUserLeft) // To know if the user left the chatroom.

        try {
            //This address is the way you can connect to localhost with AVD(Android Virtual Device)
            mSocket = IO.socket(Constants().BASE_URL)
            Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }
    }

    fun disconnect() {
        val data = ChatIndividualIdentification(/*SharedPreferenceManager.getStringValue(Constants().PREF_UID*/ "101", userUid)
        val jsonData = gson.toJson(data)
        mSocket.emit("unsubscribe", jsonData)
        mSocket.disconnect()
    }

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

    /** CALLBACKS SOCKETS **/

    var onConnect = Emitter.Listener {
        //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
        //send userName and roomName so that they can join the room.
        val data = ChatIndividualIdentification(/*SharedPreferenceManager.getStringValue(Constants().PREF_UID*/ "101", userUid)
        val jsonData = gson.toJson(data) // Gson changes data object to Json type.
        mSocket.emit("subscribe", jsonData)
    }

    var onNewUser = Emitter.Listener {
        val name = it[0] as String //This pass the userName!
        //val chat = Message(name, "", roomName, MessageType.USER_JOIN.index)
        //addItemToRecyclerView(chat)
        //Log.d(TAG, "on New User triggered.")
    }


    var onUserLeft = Emitter.Listener {
        val leftUserName = it[0] as String
        //val chat: Message = Message(leftUserName, "", "", MessageType.USER_LEAVE.index)
        //addItemToRecyclerView(chat)
    }

    var onUpdateChat = Emitter.Listener {
        val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
        listMessages.value?.add(chat)
    }
}
