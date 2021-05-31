package com.offhome.app.ui.explore


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.ui.infoactivity.InfoActivity
import java.util.*
import kotlin.collections.ArrayList


/**
 * Adpter for the recycler view of the activities list
 * @param context is the context of the activity
 * @property activitiesList is the list of activities
 */
class ActivitiesFriendsListRecyclerViewAdapter(private val context: Context?) : RecyclerView.Adapter<ActivitiesFriendsListRecyclerViewAdapter.ViewHolder>() {

    /**
     * Onclick to item.
     */
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as ActivityFromList
        val intent = Intent(context, InfoActivity::class.java)
        intent.putExtra("activity", GsonBuilder().create().toJson(item))
        context?.startActivity(intent)
    }
    private var activitiesList: List<ActivityFromList> = ArrayList()

    /**
     * it inflates the view of each activity and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_activity_friends, parent, false)
        return ViewHolder(view)
    }

    /**
     * It sets the data to each view
     * @param holder is the view holder of that view
     * @param position is the position of the view to render
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = activitiesList[position]
        holder.textViewName.text = item.titol
        holder.textViewDataTime.text = item.dataHoraIni
        holder.textViewCreated.text = context!!.getString(R.string.created_by) + " " + item.usuariCreador
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_access_time_filled_24).centerCrop().into(holder.dataTimeImage)
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_face_24).centerCrop().into(holder.createdImage)
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_border_24).centerCrop().into(holder.iconLikeImage)

        with(holder.background) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
        var clicked = false

        holder.iconLikeImage.setOnClickListener {
            clicked = !clicked
            if (clicked) {
                Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_24).centerCrop().into(holder.iconLikeImage)
            } else {
                Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_border_24).centerCrop().into(holder.iconLikeImage)
            }
        }
    }

    /**
     * gets the number of views
     * @return the number of views
     */
    override fun getItemCount(): Int = activitiesList.size

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param activitiesList is the new list of activites to set
     */
    fun setData(activitiesList: List<ActivityFromList>?) {
        this.activitiesList = activitiesList!!
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class to save the refereces to the views of each view
     * @param mView is the general view
     * @property textViewName is the textView where we will render the activity name
     * @property textViewDataTime is the textView where we will render the data and time of the activity
     * @property textViewCapacity is the textView where we will render the capacity of the activity
     * @property dataTimeImage is the image to show next to the data and time
     * @property capacityImage is the image to show next to the capacity
     * @property iconLikeImage is the image to show on the activity to like it
     * @property background is the cardview's background where all the information is displayed
     */
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewName: TextView = mView.findViewById(R.id.textViewNameActivity)
        val textViewDataTime: TextView = mView.findViewById(R.id.textViewDataTimeActivity)
        val textViewCreated: TextView = mView.findViewById(R.id.textViewCreatedBy)
        val dataTimeImage: ImageView = mView.findViewById(R.id.imageViewDataTime)
        val createdImage: ImageView = mView.findViewById(R.id.imageViewCreatedBy)
        val iconLikeImage: ImageView = mView.findViewById(R.id.imageViewIconLike)
        val background: CardView = mView.findViewById(R.id.cardViewBackground)

        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }
}
