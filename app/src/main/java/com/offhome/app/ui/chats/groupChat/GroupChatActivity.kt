package com.offhome.app.ui.chats.groupChat



import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.offhome.app.MainActivity
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.MyApp
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
    private var myRef = database.getReference("xatsGrupals/101_26-5-2000 18:00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val arguments = intent.extras
        title = arguments?.getString("This is the title of the activity")
        val user_id = "101"
        val date_ini = "26-5-2000 18:00"
        val user_name =  SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)

        messagesList = findViewById(R.id.messages_view)
        editTextNewMessage = findViewById(R.id.new_message)
        btnSendMessage = findViewById(R.id.sendButton)

        messagesAdapter = MyGroupChatRecyclerViewAdapter()
        with(messagesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }

        viewModel = ViewModelProvider(this, SingleChatViewModelFactory()).get(GroupChatViewModel::class.java)
/*
        viewModel.listMessages.observe(
            this,
            {
                messagesAdapter.setData(it)
            }
        )
        user_id.let {
            viewModel.getMessages(it, date_ini, this)
        }

        btnSendMessage.setOnClickListener {
            if (!editTextNewMessage.text.isEmpty()) {
                val mess = GroupMessage(
                    user_id, date_ini, user_id, "practico couchsurfing"
                )
                viewModel.sendMessage(mess)
            }
        }*/

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
                                user_id,
                                "Maria",
                                user_id,
                                date_ini,
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
                            user_id,
                            it1,
                            user_id,
                            date_ini,
                            System.currentTimeMillis()
                        )
                    }
                    myRef.child("m$numMessages").setValue(message)
                    editTextNewMessage.text.clear()
                }
            }
        }
    }

    /**
     * Function to specify the options menu for an activity
     * @param menu provided
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return true
    }

    /**
     * Function called when the user selects an item from the options menu
     * @param item selected
     * @return true if the menu is successfully handled
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {

            val logout_dialog = AlertDialog.Builder(this)
            logout_dialog.setTitle(R.string.dialog_logout_title)
            logout_dialog.setMessage(R.string.dialog_logout_message)
            logout_dialog.setPositiveButton(R.string.ok) { dialog, id ->
                //aqui va el que fem per abandonar un xat grupal
                myRef = database.getReference("usuaris/${SharedPreferenceManager.getStringValue(Constants().PREF_UID)}")

                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (chatSnapshot in dataSnapshot.children) {
                            if (chatSnapshot.value!!.equals("101_26-5-2000 18:00")) {
                                chatSnapshot.ref.removeValue()
                                    startActivity(Intent(MyApp.getContext(), MainActivity::class.java))
                                    finish()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })

                /* para enviar a otra pantalla, la de todos los xats, next sprint
                requireActivity().run {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }*/
            }
            logout_dialog.setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.dismiss()
            }
            logout_dialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

}
