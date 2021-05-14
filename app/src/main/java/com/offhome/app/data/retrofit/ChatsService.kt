package com.offhome.app.data.retrofit



import com.offhome.app.data.model.ChatGroupIdentification
import com.offhome.app.data.model.SendMessage
import com.offhome.app.model.GroupMessage
import com.offhome.app.model.Message
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ChatsService {
    /**
     * This is the call for getting the messages of a individual chat
     */
    @GET("xats/individual")
    fun getAllMessages(@Query("usid_1") usid1: String, @Query("usid_2") usid2: String): Call<List<Message>>

    @POST("xats/missatgesIndividual")
    fun sendMessage(@Body message: SendMessage): Call<ResponseBody>

    /**
     * This is the call for getting the messages of a group chat
     */
    @HTTP(method = "GET", path = "xats/grupal", hasBody = true)
    fun getAllMessagesGroup(@Body chat: ChatGroupIdentification?): Call<List<GroupMessage>>

    @HTTP(method = "POST", path = "xats/missatgesGrup", hasBody = true)
    fun sendGroupMissage(@Body chat: GroupMessage?): Call<ResponseBody>
    /**
     * This is the call for creating a new group chat
     */
    @HTTP(method = "POST", path = "xats/crearGrup", hasBody = true)
    fun addChatGroup(@Body chat: ChatGroupIdentification): Call<ResponseBody>

}
