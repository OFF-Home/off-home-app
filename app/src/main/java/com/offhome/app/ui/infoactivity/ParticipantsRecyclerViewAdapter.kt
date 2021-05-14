package com.offhome.app.ui.infoactivity



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.offhome.app.R

/**
 * Adpter for the recycler view of the activities list
 * @property participantsList is the list of activities
 */
class ParticipantsRecyclerViewAdapter() : RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.ViewHolder>() {

    /*repasar esto
    /**
     * Onclick to item.
     */
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as UserInfo
        val intent = Intent(context, OtherProfileActivity::class.java)
        intent.putExtra("user_info", GsonBuilder().create().toJson(item))
        context?.startActivity(intent)
    }*/

    private var participantsList: List<String> = ArrayList()

    /**
     * it inflates the view of each participant and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_participant, parent, false)
        return ViewHolder(view)
    }

    /**
     * It sets the data to each view
     * @param holder is the view holder of that view
     * @param position is the position of the view to render
     */
    override fun onBindViewHolder(holder: ParticipantsRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = participantsList[position]
        holder.textViewUsername.text = item
        // aqui hay que pedir profile pic realmente
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_people_alt_24).centerCrop().into(holder.profilepic)
    }

    /**
     * gets the number of views
     * @return the number of views
     */
    override fun getItemCount(): Int = participantsList.size

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param participantsList is the new list of activites to set
     */
    fun setData(participantsList: List<String>?) {
        this.participantsList = participantsList!!
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class to save the refereces to the views of each view
     * @param mView is the general view
     * @property textViewUsername is the textView where we will render the user's name
     * @property profilepic is the image to show on of the user
     */
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewUsername: TextView = mView.findViewById(R.id.participant_username)
        val profilepic: ImageView = mView.findViewById(R.id.participant_profile_pic)

        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }
}
