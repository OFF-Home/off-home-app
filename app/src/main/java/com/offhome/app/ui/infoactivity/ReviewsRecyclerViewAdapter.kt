package com.offhome.app.ui.infoactivity



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.data.model.ReviewOfParticipant

/**
 * Adpter for the recycler view of the activities list
 * @property reviewsList is the list of reviews of all the participants
 */
class ReviewsRecyclerViewAdapter() : RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder>() {

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

    private var reviewsList: List<ReviewOfParticipant> = ArrayList()

    /**
     * it inflates the view of each participant and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_comments, parent, false)
        return ViewHolder(view)
    }

    /**
     * It sets the data to each view
     * @param holder is the view holder of that view
     * @param position is the position of the view to render
     */
    override fun onBindViewHolder(holder: ReviewsRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = reviewsList[position]
        holder.textViewUsername.text = item.username
        holder.textViewComment.text = item.comentari
    }

    /**
     * gets the number of views
     * @return the number of views
     */
    override fun getItemCount(): Int = reviewsList.size

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param reviewsList is the new list of reviews of all the participants
     */
    fun setData(reviewsList: List<ReviewOfParticipant>?) {
        this.reviewsList = reviewsList!!
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class to save the refereces to the views of each view
     * @param mView is the general view
     * @property textViewUsername is the textView where we will render the user's name
     * @property textViewComment is the textView where we will render the user's comment
     */
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewUsername: TextView = mView.findViewById(R.id.participant_username)
        val textViewComment: TextView = mView.findViewById(R.id.participant_comment)

        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }
}
