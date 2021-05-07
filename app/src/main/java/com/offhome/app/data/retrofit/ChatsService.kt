package com.offhome.app.data.retrofit



import com.offhome.app.data.model.ChatGroupIdentification
import com.offhome.app.data.model.ChatIndividualIdentification
import com.offhome.app.model.Message
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP

interface ChatsService {
    /**
     * This is the call for getting the messages of a individual chat
     */
    @HTTP(method = "GET", path = "xats/individual", hasBody = true)
    fun getAllMessages(@Body chat: ChatIndividualIdentification?): Call<List<Message>>

    /**
     * This is the call for getting the messages of a group chat
     */
    @HTTP(method = "GET", path = "xats/grupal", hasBody = true)
    fun getAllMessagesGroup(@Body chat: ChatGroupIdentification?): Call<List<Message>>


/*
    @HTTP(method = "POST", path = "xats/missatgesGrup", hasBody = true)
    fun sendGroupMissage(@Body chat: Message?): Call<ResponseBody>
    com poso el body? al model Message s'hi hauria d'afegir atributs
    */
}
