package com.offhome.app.ui.chats.singleChat



import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.data.Result
import com.offhome.app.ui.chats.MyChatRecyclerViewAdapter

class SingleChatActivity : AppCompatActivity() {
    private lateinit var messagesAdapter: MyChatRecyclerViewAdapter
    private lateinit var viewModel: SingleChatViewModel

    private lateinit var messagesList: RecyclerView
    private lateinit var editTextNewMessage: EditText
    private lateinit var btnSendMessage: ImageButton

    private lateinit var userUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arguments = intent.extras
        userUid = "102" // arguments?.getString("uid")
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

        viewModel.initializeSocket(userUid, this)

        viewModel.listMessages.observe(
            this,
            {
                messagesAdapter.setData(it)
            }
        )
        /*
        userUid?.let {
            viewModel.getMessages(
                /*SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)!!*/"101",
                it, this
            )
        } ?: run {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
*/
        viewModel.sendMessageResult.observe(
            this,
            {
                if (it is Result.Error) Toast.makeText(this, getString(R.string.error_send_message), Toast.LENGTH_LONG).show()
            }
        )

        btnSendMessage.setOnClickListener {
            if (editTextNewMessage.text.isEmpty())
                Toast.makeText(
                    this, getString(R.string.error_empty_message),
                    Toast.LENGTH_LONG
                ).show()
            else viewModel.sendMessage(
                /*SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)!!*/"101",
                userUid, editTextNewMessage.text.toString()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnectFromSocket()
    }
}
