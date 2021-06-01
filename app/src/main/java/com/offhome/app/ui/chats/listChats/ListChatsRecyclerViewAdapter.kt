package com.offhome.app.ui.chats.listChats

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.data.model.ChatInfo
import com.offhome.app.ui.chats.groupChat.GroupChatActivity
import com.offhome.app.ui.chats.singleChat.SingleChatActivity
import com.offhome.app.ui.infoactivity.InfoActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adpter for the recycler view of the chat list
 * @property listChats is the list of activities
 * @author Pau Cuesta
 */
class ListChatsRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<ListChatsRecyclerViewAdapter.ViewHolder>() {

    private var listChats: List<ChatInfo> = ArrayList()

    /**
     * Onclick to item.
     */
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as ChatInfo
        if (item.id.contains("_")) {
            val intent = Intent(context, GroupChatActivity::class.java)
            intent.putExtra("titleAct", item.name)
            intent.putExtra("usuariCreador", item.id.split("_")[0])
            intent.putExtra("dataHI", item.id.split("_")[1])
            context.startActivity(intent)
        } else {
            val intent = Intent(context, SingleChatActivity::class.java)
            intent.putExtra("uid", item.id)
            intent.putExtra("username", item.name)
            context.startActivity(intent)
        }
    }

    /**
     * it inflates the view of each chat and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_chat, parent, false)
        return ViewHolder(view)
    }

    /**
     * It sets the data to each view
     * @param holder is the view holder of that view
     * @param position is the position of the view to render
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listChats[position]
        holder.textViewName.text = item.name
        Glide.with(holder.mView.context)
            .load(item.image)
            .placeholder(R.drawable.profile_pic_placeholder)
            .centerCrop()
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.imageViewIcon)

        with(holder.chat) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    /**
     * gets the number of views
     * @return the number of views
     */
    override fun getItemCount(): Int = listChats.size

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param activitiesList is the new list of activites to set
     */
    fun setData(newList: List<ChatInfo>?) {
        this.listChats = newList!!
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class to save the refereces to the views of each view
     * @param mView is the general view
     */
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewName: TextView = mView.findViewById(R.id.textViewNameChat)
        val imageViewIcon: ImageView = mView.findViewById(R.id.imageViewIconChat)
        val chat: ConstraintLayout = mView.findViewById(R.id.containerChat)
        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }
}
