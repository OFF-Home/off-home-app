package com.offhome.app.ui.chats.listChats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.data.model.ChatInfo
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adpter for the recycler view of the chat list
 * @property listChats is the list of activities
 */
class ListChatsRecyclerViewAdapter() : RecyclerView.Adapter<ListChatsRecyclerViewAdapter.ViewHolder>() {

    private var listChats: List<ChatInfo> = ArrayList()

    /**
     * Onclick to item.
     */
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as ChatInfo
    }

    /**
     * it inflates the view of each chat and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_chats_fragment, parent, false)
        return ViewHolder(view)
    }

    /**
     * It sets the data to each view
     * @param holder is the view holder of that view
     * @param position is the position of the view to render
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listChats[position]
        //Glide.with(holder.mView.context).load(R.drawable.ic_baseline_access_time_filled_24).centerCrop().into(holder.dataTimeImage)

//        with(holder.background) {
//            tag = item
//            setOnClickListener(mOnClickListener)
//        }
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

        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }
}
