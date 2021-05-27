package com.offhome.app.ui.chats.singleChat



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.MyApp
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.model.Message

/**
 * Adpter for the recycler view of messages of a chat
 * @property listMessages is the list of Messages
 */
class MyChatRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listMessages: List<Message> = ArrayList()

    inner class ViewHolderMessage(mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewMessage: TextView = mView.findViewById(R.id.textViewMessage)
        val imageViewPerson: ImageView = mView.findViewById(R.id.imageViewPhoto)
    }

    /**
     * It assignes the ViewHolder. It depends on the message, if is a self message or not
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_message_i, parent, false)
                ViewHolderMessage(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_message_other, parent, false)
                ViewHolderMessage(view)
            }
        }
    }

    /**
     * It loads the view. It depends on the message, if is a self message or not
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listMessages[position]
        (holder as ViewHolderMessage).textViewMessage.text = item.message
        (holder as ViewHolderMessage).textViewMessage.setOnLongClickListener {
            // Delete message
            Toast.makeText(MyApp.getContext(), "Long press", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }
        // TODO Load image of a user
        Glide.with(MyApp.getContext()).load(R.drawable.profile_pic_placeholder).centerCrop().circleCrop().into(holder.imageViewPerson)
    }

    /**
     * Returns the number of messages
     */
    override fun getItemCount(): Int = listMessages.size

    /**
     * It sets the type of view: if it is a message that the users has sent or recieved
     */
    override fun getItemViewType(position: Int): Int {
        return if (listMessages.get(position).usid_enviador == SharedPreferenceManager.getStringValue(
                Constants().PREF_UID
            )
        ) 0
        else 1
    }

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param messages is the new list of messages to set
     */
    fun setData(messages: List<Message>?) {
        this.listMessages = messages!!
        notifyDataSetChanged()
    }
}
