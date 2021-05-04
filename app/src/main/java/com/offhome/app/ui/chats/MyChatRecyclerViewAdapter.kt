package com.offhome.app.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.MyApp
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.Message

class MyChatRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listMessages : List<Message> = ArrayList()

    inner class ViewHolderMessage(private val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewMessage: TextView = mView.findViewById(R.id.textViewMessage)
        val imageViewPerson: ImageView = mView.findViewById(R.id.imageViewPhoto)
    }

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listMessages[position]
        (holder as ViewHolderMessage).textViewMessage.text = item.message
        // TODO Load image of a user
        Glide.with(MyApp.getContext()).load(R.drawable.profile_pic_placeholder).centerCrop().circleCrop().into(holder.imageViewPerson)
    }

    override fun getItemCount(): Int = listMessages.size

    override fun getItemViewType(position: Int): Int {
        return if (listMessages.get(position).user == SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL)) 0
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
