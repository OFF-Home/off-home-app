package com.offhome.app.ui.chats.groupChat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.model.GroupMessage
import com.offhome.app.ui.chats.singleChat.SingleChatViewModelFactory

class GroupChatActivity : AppCompatActivity() {
    private lateinit var messagesAdapter: MyGroupChatRecyclerViewAdapter
    private lateinit var viewModel: GroupChatViewModel

    private lateinit var messagesList: RecyclerView
    private lateinit var editTextNewMessage: EditText
    private lateinit var btnSendMessage: ImageButton

    private var numMessages = 0
    val database = Firebase.database
    private lateinit var myRef: DatabaseReference

    /**
     * It is called when creating the activity and has all the connection with database
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val arguments = intent.extras

        val userUid = arguments?.getString("usuariCreador")
        val data_ini = arguments?.getString("dataHI")
        val user_name =  SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)
        val uid_user = SharedPreferenceManager.getStringValue(Constants().PREF_UID)

        messagesList = findViewById(R.id.messages_view)
        editTextNewMessage = findViewById(R.id.new_message)
        btnSendMessage = findViewById(R.id.sendButton)

        myRef = database.getReference("xatsGrupals/${userUid}_${data_ini}")

        messagesAdapter = MyGroupChatRecyclerViewAdapter()
        with(messagesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }

        viewModel = ViewModelProvider(this, SingleChatViewModelFactory()).get(GroupChatViewModel::class.java)

        myRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listMessages = ArrayList<GroupMessage>()
                numMessages = dataSnapshot.childrenCount.toInt()
                val iterator = dataSnapshot.children.iterator()
                while (iterator.hasNext()) {
                    listMessages.add(iterator.next().getValue(GroupMessage::class.java) as GroupMessage)
                }
                messagesAdapter.setDataGroup(listMessages)
                messagesList.scrollToPosition(messagesList.adapter!!.itemCount - 1)
                // messagesAdapter.setData(value)
                // Log.d("RESPONSE", value)
                // messagesAdapter.setData(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        viewModel.sendMessageResult.observe(
            this,
            {
                if (it is Result.Error) Toast.makeText(
                    this,
                    getString(R.string.error_send_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        //else gestionar notificacions
        )

        editTextNewMessage.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (editTextNewMessage.text.isEmpty())
                            Toast.makeText(
                                context, getString(R.string.error_empty_message),
                                Toast.LENGTH_LONG
                            ).show()
                        else {
                            ++numMessages
                            val message = GroupMessage(
                                editTextNewMessage.text.toString(),
                                userUid.toString(),
                                user_name.toString(),
                                uid_user.toString(),
                                data_ini.toString(),
                                System.currentTimeMillis()
                            )
                            myRef.child("m$numMessages").setValue(message)
                            editTextNewMessage.text.clear()
                        }
                }
                false
            }
            btnSendMessage.setOnClickListener {
                if (editTextNewMessage.text.isEmpty())
                    Toast.makeText(
                        context, getString(R.string.error_empty_message),
                        Toast.LENGTH_LONG
                    ).show()
                else {
                    ++numMessages
                    val message = user_name?.let { it1 ->
                        GroupMessage(
                            editTextNewMessage.text.toString(),
                            userUid.toString(),
                            it1,
                            uid_user.toString(),
                            data_ini.toString(),
                            System.currentTimeMillis()
                        )
                    }
                    myRef.child("m$numMessages").setValue(message)
                    editTextNewMessage.text.clear()
                }
            }
        }
    }
}
