package com.offhome.app.ui.inviteChoosePerson

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

    override fun onBindViewHolder(holder: UsersListRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = userList[position]
        holder.textViewUsername.text = item.username
        holder.textViewEmail.text = item.email
        //todo la foto

        with(holder.layout) {
            tag = item
            setOnClickListener(elementLayoutOnClickListener)
        }

    }

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

    }
}

