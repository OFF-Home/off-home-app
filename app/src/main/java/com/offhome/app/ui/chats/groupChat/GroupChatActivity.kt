package com.offhome.app.ui.chats.groupChat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.ui.chats.MyChatRecyclerViewAdapter

class GroupChatActivity: AppCompatActivity() {
    private lateinit var messagesAdapter: MyChatRecyclerViewAdapter
    private lateinit var viewModel: GroupChatViewModel

    private lateinit var messagesList: RecyclerView
    private lateinit var editTextNewMessage: EditText
    private lateinit var btnSendMessage: ImageButton


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val arguments = intent.extras
        title = arguments?.getString("This is the title of the activity")
        val user_id = "101"
        val date_ini = "26-5-2000 18:00"

        messagesList = findViewById(R.id.messages_view)
        editTextNewMessage = findViewById(R.id.new_message)
        btnSendMessage = findViewById(R.id.sendButton)

        messagesAdapter = MyChatRecyclerViewAdapter()
        with(messagesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }

        viewModel = ViewModelProvider(this).get(GroupChatViewModel::class.java)

        viewModel.listMessages.observe(
            this,
            {
                messagesAdapter.setData(it)
            }
        )
        user_id.let {
            viewModel.getMessages(it,date_ini)
        }

        btnSendMessage.setOnClickListener {
            if (!editTextNewMessage.text.isEmpty()) {
                viewModel.sendMessage(it.toString(), date_ini, it.toString(), "practico couchsurfing")
            }
        }
    }
}
