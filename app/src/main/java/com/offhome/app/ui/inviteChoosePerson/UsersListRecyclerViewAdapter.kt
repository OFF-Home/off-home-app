package com.offhome.app.ui.inviteChoosePerson

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.offhome.app.R
import com.offhome.app.model.profile.UserSummaryInfo

class UsersListRecyclerViewAdapter(private val context: Context?) : RecyclerView.Adapter<UsersListRecyclerViewAdapter.ViewHolder>() {

    private var userList: List<UserSummaryInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListRecyclerViewAdapter.ViewHolder {    //int?
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_recipient, parent, false)
        return ViewHolder(view)
    }

    private val elementLayoutOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as UserSummaryInfo
        //val intent = Intent(context, InfoActivity::class.java)
        //intent.putExtra("activity", GsonBuilder().create().toJson(item))
        //context?.startActivity(intent)
        Snackbar.make(v, "contact tapped", Snackbar.LENGTH_SHORT).show()
    }

    //re-done a try3
    /*override fun onBindViewHolder(holder: UsersListRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = userList[position]
        holder.textViewUsername.text = item.username
        holder.textViewEmail.text = item.email
        //todo la foto

        with(holder.layout) {
            tag = item
            setOnClickListener(elementLayoutOnClickListener)
        }
    }*/

    override fun getItemCount(): Int = userList.size

    fun setData(userList: List<UserSummaryInfo>?) {
        this.userList = userList!!
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewUsername: TextView = mView.findViewById(R.id.recipient_username)
        val textViewEmail: TextView = mView.findViewById(R.id.recipient_email)
        val imageViewProfilePic: ImageView = mView.findViewById(R.id.recipient_profile_pic)
        val layout : ConstraintLayout = mView.findViewById(R.id.recipient_item_layout)

        //3r try
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }

        fun bind(value: UserSummaryInfo, isActivated: Boolean = false) {
            //text.text = value.toString()
            //textViewUsername.text = value.toString()    //nose xd
            textViewUsername.text = value.username
            textViewEmail.text = value.email
            itemView.isActivated = isActivated      //lol?
        }
    }

    //3r try
    init {
        setHasStableIds(true)
    }
    override fun getItemId(position: Int): Long = position.toLong()

    var tracker: SelectionTracker<Long>? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipient = userList[position]
        tracker?.let {
            holder.bind(recipient, it.isSelected(position.toLong()))
        }
    }

}

