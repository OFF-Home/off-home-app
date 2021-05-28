package com.offhome.app.ui.oldandliked



import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList
import com.offhome.app.ui.infoactivity.InfoActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adpter for the recycler view of the old activities list
 * @param context is the context of the activity
 * @property oldActivitiesList is the list of old activities
 */
class OldActivitiesListRecyclerViewAdapter(private val context: Context?) : RecyclerView.Adapter<OldActivitiesListRecyclerViewAdapter.ViewHolder>() {

    /**
     * Onclick to item.
     */
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as ActivityFromList
        val intent = Intent(context, InfoActivity::class.java)
        intent.putExtra("activity", GsonBuilder().create().toJson(item))
        context?.startActivity(intent)
    }
    private var oldActivitiesList: List<ActivityFromList> = ArrayList()

    /**
     * it inflates the view of each activity and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_oldact, parent, false)
        return ViewHolder(view)
    }

    /**
     * It sets the data to each view
     * @param holder is the view holder of that view
     * @param position is the position of the view to render
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = oldActivitiesList[position]
        holder.textViewName.text = item.titol
        holder.textViewDataTime.text = item.dataHoraIni
        //holder.textViewCapacity.text = item.participants.toString() + "/" + item.maxParticipant.toString()
        holder.textViewCapacity.text = item.maxParticipant.toString()
        holder.stars.setRating((item.valoracio.toFloat()))
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_access_time_filled_24).centerCrop().into(holder.dataTimeImage)
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_people_alt_24).centerCrop().into(holder.capacityImage)

        with(holder.background) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    /**
     * gets the number of views
     * @return the number of views
     */
    override fun getItemCount(): Int = oldActivitiesList.size

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param oldActivitiesList is the new list of activites to set
     */
    fun setData(oldActivitiesList: List<ActivityFromList>?) {
        this.oldActivitiesList = oldActivitiesList!!
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
     * @property stars is the rating bar with the rating of the activity
     * @property background is the cardview's background where all the information is displayed
     */
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewName: TextView = mView.findViewById(R.id.textViewNameActivity)
        val textViewDataTime: TextView = mView.findViewById(R.id.textViewDataTimeActivity)
        val textViewCapacity: TextView = mView.findViewById(R.id.textViewCapacity)
        val dataTimeImage: ImageView = mView.findViewById(R.id.imageViewDataTime)
        val capacityImage: ImageView = mView.findViewById(R.id.imageViewCapacity)
        val stars: RatingBar = mView.findViewById(R.id.ratingStars)
        val background: CardView = mView.findViewById(R.id.cardViewBackground)

        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }
}
