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
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker

//todo em caldrà descomentar el list dels params? llavors he de igualar el userList a aquest.
class UsersListRecyclerViewAdapter(/*private val selectedUserList: List<UserSummaryInfo>,*/ private val context: Context?) : RecyclerView.Adapter<UsersListRecyclerViewAdapter.ViewHolder>() {

    private var userList: List<UserSummaryInfo> = ArrayList()
    //private val selectedItems: SparseBooleanArray? = null
    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }

    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    /*fun isSelected(position: Int): Boolean {
        return getSelectedItems().contains(position)
    }*/

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

        //per la seleccio
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object: ItemDetailsLookup.ItemDetails<Long>() {

                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
                // More code here

            }
    }
    //potser fer-la fora. pero necessita referenciar el ViewHolder
    class InviteLookup(private val rv: RecyclerView) : ItemDetailsLookup<String>() {
        override fun getItemDetails(event: MotionEvent) : ItemDetails<String>? {
            // More code here
            val view = rv.findChildViewUnder(event.x, event.y)
            if(view != null) {
                return (rv.getChildViewHolder(view) as ViewHolder).getItemDetails()         //ojo q aquest ViewHolder que menciona és el inner de dins de UsersListRecyclerViewAdapter
            }
            return null
        }
    }
}

