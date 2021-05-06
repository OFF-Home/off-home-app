package com.offhome.app.data.retrofit

import com.offhome.app.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Chat Client Class
 * It has the base configuration of retrofit for the chats
 * @property instance has the instance of this client
 * @property chatsService is the instance of the service that connects with backend
 * @property retrofit is the instance of retrofit library to reach backend
 */
class ChatClient {
    private lateinit var instance: ChatClient
    private var chatsService: ChatsService? = null
    private var retrofit: Retrofit? = null

    /**
     * Inits all configuration, such as the baseURL or the GSon converter
     */
    init {
        retrofit = Retrofit.Builder()
            .baseUrl(Constants().BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        chatsService = retrofit!!.create(ChatsService::class.java)
    }

    /**
     * Returns the instance of the client
     */
    fun getInstance(): ChatClient {
        return instance
    }

    /**
     * Returns the instance of the service
     */
    fun getChatsService(): ChatsService? {
        return chatsService
    }
}
