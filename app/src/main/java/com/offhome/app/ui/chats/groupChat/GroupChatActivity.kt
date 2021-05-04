package com.offhome.app.ui.chats.groupChat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.offhome.app.R

class GroupChatActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //val nameAct = intent.getParcelableExtra<Activity>()
        val arguments = intent.extras
        title = arguments?.getString("This is the title of the activity")

        // val intent: Intent()
       // groupId = intent.getStringExtra()

    }
}
