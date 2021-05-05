package com.offhome.app.ui.chats.singleChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.MainActivity
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.ui.chats.MyChatRecyclerViewAdapter

class SingleChatActivity : AppCompatActivity() {
    private lateinit var messagesAdapter: MyChatRecyclerViewAdapter
    private lateinit var viewModel: SingleChatViewModel

    private lateinit var messagesList: RecyclerView
    private lateinit var editTextNewMessage: EditText
    private lateinit var btnSendMessage: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arguments = intent.extras
        val userUid = arguments?.getString("uid")
        val userName = arguments?.getString("username")
        title = userName

        editTextNewMessage = findViewById(R.id.editTextNewMessage)
        btnSendMessage = findViewById(R.id.imageButtonSendMessage)
        messagesList = findViewById(R.id.recyclerViewMessages)
        messagesAdapter = MyChatRecyclerViewAdapter()
        with(messagesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }

        viewModel = ViewModelProvider(this).get(SingleChatViewModel::class.java)

        viewModel.listMessages.observe(this, {
            messagesAdapter.setData(it)
        })

        userUid?.let {
            viewModel.getMessages(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)!!,
                it
            )
        } ?: run {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnSendMessage.setOnClickListener {
            if (editTextNewMessage.text.isEmpty())
                Toast.makeText(this, getString(R.string.error_empty_message),
                    Toast.LENGTH_LONG).show()
            else viewModel.sendMessage(editTextNewMessage.text.toString())
        }
    }
}
