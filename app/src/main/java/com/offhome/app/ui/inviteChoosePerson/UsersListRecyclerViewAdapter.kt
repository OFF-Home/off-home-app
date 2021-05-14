package com.offhome.app.ui.inviteChoosePerson



import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.offhome.app.model.Category
import com.offhome.app.model.profile.UserInfo
import com.offhome.app.model.profile.UserSummaryInfo
import java.util.*
import kotlin.collections.ArrayList

class UsersListRecyclerViewAdapter(private val context: Context?) : RecyclerView.Adapter<UsersListRecyclerViewAdapter.ViewHolder>() {

    var userList: List<UserSummaryInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListRecyclerViewAdapter.ViewHolder { // int?
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_recipient, parent, false)
        return ViewHolder(view)
    }

    private val elementLayoutOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as UserSummaryInfo
        // val intent = Intent(context, InfoActivity::class.java)
        // intent.putExtra("activity", GsonBuilder().create().toJson(item))
        // context?.startActivity(intent)
        Snackbar.make(v, "contact tapped", Snackbar.LENGTH_SHORT).show()
    }

    // re-done a try3
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
        private val textViewUsername: TextView = mView.findViewById(R.id.recipient_username)
        private val textViewEmail: TextView = mView.findViewById(R.id.recipient_email)
        val imageViewProfilePic: ImageView = mView.findViewById(R.id.recipient_profile_pic)
        val layout: ConstraintLayout = mView.findViewById(R.id.recipient_item_layout)

        // 3r try
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId

                override fun inSelectionHotspot(e: MotionEvent): Boolean {
                    return true
                }
            }

        fun bind(value: UserSummaryInfo, isActivated: Boolean = false) {
            textViewUsername.text = value.username
            textViewEmail.text = value.email
            itemView.isActivated = isActivated // lol?
        }
    }

    // 3r try
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

    fun performFiltering(constraint: String?/*, completeUserList:ArrayList<UserInfo>*/) {
        val charSearch = constraint.toString()

        val userList2:List<UserSummaryInfo> //pot ser innecessaria peor weno
        if (charSearch.isEmpty()) { //si buscador buit, mostrar la original (aka no tocar)
            userList2 = userList
        }
        else {
            val resultList = ArrayList<UserSummaryInfo>()
            for (row in /*completeUserList*/userList) { //per tota fila de la llista de TOTS els users
                if (row.username.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT)))    //si conté la string: el mostrem.
                    resultList.add(UserSummaryInfo(email = row.email, username = row.username, uid = row.uid))
            }
            userList2 = resultList
        }

        userList = userList2
        notifyDataSetChanged()
    }
}
