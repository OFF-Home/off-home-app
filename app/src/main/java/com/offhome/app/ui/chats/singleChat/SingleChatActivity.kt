package com.offhome.app.ui.chats.singleChat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.offhome.app.R

class SingleChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arguments = intent.extras
        val userUid = arguments?.getString("uid")
        val userName = arguments?.getString("username")
        title = userName
    }
}
