package com.offhome.app.ui.chats.groupChat

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class ChatMessage: AppCompatActivity() {
    var messageText: String? = null
    var messageUser: String? = null
    var messageTime: Long = 0
    private lateinit var sendBtn: Button
    private lateinit var writeMess: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        constructor("", "")
        setContentView(com.offhome.app.R.layout.activity_group_chat)

        //aquí guardem el botó d'enviar i el edittext del missatge (de la LinearLayout de activity_group_chat)
        /*sendBtn = findViewById(R.id.sendButton)
        send_btn.setOnClickListener {
            writeMess = findViewById(R.id.new_message)
        }*/
    }

    private fun constructor(messageText: String?, messageUser: String?) {
        this.messageText = messageText
        this.messageUser = messageUser

        // Initialize to current time
        messageTime = Date().getTime()
    }

}
