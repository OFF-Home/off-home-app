package com.offhome.app.ui.chats.singleChat



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
import com.offhome.app.model.Message

class SingleChatActivity : AppCompatActivity() {
    private lateinit var messagesAdapter: MyChatRecyclerViewAdapter
    private lateinit var viewModel: SingleChatViewModel

    private lateinit var messagesList: RecyclerView
    private lateinit var editTextNewMessage: EditText
    private lateinit var btnSendMessage: ImageButton

    private var numMessages = 0
    val database = Firebase.database
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arguments = intent.extras
        val userUid = arguments?.getString("uid")
        val userName = arguments?.getString("username")
        title = userName
        myRef =  database.getReference("xatsIndividuals/${SharedPreferenceManager.getStringValue(Constants().PREF_UID)}_${userUid}")
        editTextNewMessage = findViewById(R.id.editTextNewMessage)
        btnSendMessage = findViewById(R.id.imageButtonSendMessage)
        messagesList = findViewById(R.id.recyclerViewMessages)
        messagesAdapter = MyChatRecyclerViewAdapter()
        with(messagesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }

        viewModel = ViewModelProvider(
            this,
            SingleChatViewModelFactory()
        ).get(SingleChatViewModel::class.java)

        // viewModel.initializeSocket(userUid, this)

        myRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var listMessages = ArrayList<Message>()
                numMessages = dataSnapshot.childrenCount.toInt()
                val iterator = dataSnapshot.children.iterator()
                while (iterator.hasNext()) {
                    listMessages.add(iterator.next().getValue(Message::class.java) as Message)
                }
                messagesAdapter.setData(listMessages)
                messagesList.scrollToPosition(messagesList.adapter!!.itemCount - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

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
                            val message = Message(
                                editTextNewMessage.text.toString(),
                                SharedPreferenceManager.getStringValue(Constants().PREF_UID)!!,
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
                    val message = Message(
                        editTextNewMessage.text.toString(),
                        SharedPreferenceManager.getStringValue(Constants().PREF_UID)!!,
                        System.currentTimeMillis()
                    )
                    myRef.child("m$numMessages").setValue(message)
                    editTextNewMessage.text.clear()
                }
            }
        }
    }
}
