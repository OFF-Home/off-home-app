package com.offhome.app.ui.chats.groupChat

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
import com.offhome.app.model.GroupMessage

class MyGroupChatRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var listGroupMessages: List<GroupMessage> = ArrayList()

        inner class ViewHolderGroupMessage(mView: View) : RecyclerView.ViewHolder(mView) {
            val nameViewPerson: TextView = mView.findViewById(R.id.userName)
            val textViewMessage: TextView = mView.findViewById(R.id.textViewMessage)
            val imageViewPerson: ImageView = mView.findViewById(R.id.imageViewPhoto)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                0 -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.chat_message_other, parent, false)
                    ViewHolderGroupMessage(view)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.chat_message_i, parent, false)
                    ViewHolderGroupMessage(view)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = listGroupMessages[position]
            (holder as ViewHolderGroupMessage).textViewMessage.text = item.message
            (holder as ViewHolderGroupMessage).textViewMessage.setOnLongClickListener {
                // Delete message
                Toast.makeText(MyApp.getContext(), "Long press", Toast.LENGTH_LONG).show()
                return@setOnLongClickListener true
            }
            (holder as ViewHolderGroupMessage).nameViewPerson.text = item.userNameSender
          //  item.userSender.also { holder.nameViewPerson.text = it }
            // TODO Load image of a user
            Glide.with(MyApp.getContext()).load(R.drawable.profile_pic_placeholder).centerCrop().circleCrop().into(holder.imageViewPerson)
        }

        override fun getItemCount(): Int = listGroupMessages.size

        override fun getItemViewType(position: Int): Int {
            return if (listGroupMessages.get(position).userSender != SharedPreferenceManager.getStringValue(
                    Constants().PREF_UID)) 0
            else 1
        }

        /**
         * sets the new data and notifies to the adapter to refresh if necessary
         * @param messages is the new list of messages to set
         */
        fun setDataGroup(messages: List<GroupMessage>?) {
            this.listGroupMessages = messages!!
            notifyDataSetChanged()
        }
}
