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
import com.offhome.app.data.model.Message

/**
 * Ativity for a single activity
 * @property messagesAdapter is the adapter list of Messages
 * @property viewModel is the viewModel of the activity
 * @property messagesList is the view of the recycler
 * @property editTextNewMessage is the EditText of the new message
 * @property btnSendMessage is the Button to send a message
 * @property numMessages is the amount of sent messages
 * @property database is the reference of the database
 * @property myRef is the reference of the part of the database to read and write
 */

class SingleChatActivity : AppCompatActivity() {
    private lateinit var messagesAdapter: MyChatRecyclerViewAdapter
    private lateinit var viewModel: SingleChatViewModel

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
        setContentView(R.layout.activity_single_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arguments = intent.extras
        val userUid = arguments?.getString("uid")
        val userName = arguments?.getString("username")
        editTextNewMessage = findViewById(R.id.editTextNewMessage)
        btnSendMessage = findViewById(R.id.imageButtonSendMessage)
        messagesList = findViewById(R.id.recyclerViewMessages)
        messagesAdapter = MyChatRecyclerViewAdapter()
        with(messagesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }
        title = userName
        myRef = database.getReference("xatsIndividuals/${userUid}_${SharedPreferenceManager.getStringValue(Constants().PREF_UID)}")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    myRef = database.getReference("xatsIndividuals/${SharedPreferenceManager.getStringValue(Constants().PREF_UID)}_$userUid")
                }
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
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        viewModel = ViewModelProvider(
            this,
            SingleChatViewModelFactory()
        ).get(SingleChatViewModel::class.java)

        // viewModel.initializeSocket(userUid, this)

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
