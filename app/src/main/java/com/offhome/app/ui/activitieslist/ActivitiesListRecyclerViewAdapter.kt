package com.offhome.app.ui.activitieslist



import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.ui.infoactivity.InfoActivity
import java.util.*
import kotlin.collections.ArrayList


/**
 * Adpter for the recycler view of the activities list
 * @param context is the context of the activity
 * @property activitiesList is the list of activities
 */
class ActivitiesListRecyclerViewAdapter(
    private val context: Context?,
    private var activitiesViewModel: ActivitiesViewModel
) : RecyclerView.Adapter<ActivitiesListRecyclerViewAdapter.ViewHolder>() {

    private var tempListAct: List<ActivityFromList> = ArrayList()
    private var listActivitiesFull: List<ActivityFromList> = ArrayList()
    private lateinit var layout: View

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
    private var likedList = ArrayList<Boolean>()

    /**
     * it inflates the view of each activity and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_activity, parent, false)
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
        // holder.textViewCapacity.text = item.participants.toString() + "/" + item.maxParticipant.toString()
        holder.textViewCapacity.text = item.maxParticipant.toString()
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_access_time_filled_24).centerCrop().into(holder.dataTimeImage)
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_people_alt_24).centerCrop().into(holder.capacityImage)
  /*      if (likedList[position] == true) Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_24).centerCrop().into(holder.iconLikeImage)
        else Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_border_24).centerCrop().into(holder.iconLikeImage)
        with(holder.background) {
            tag = item
            setOnClickListener(mOnClickListener)
        }

        holder.iconLikeImage.setOnClickListener {
            likedList[position] = !likedList[position]
            if (likedList[position]) {
                //post de donar like a una activitat
                activitiesViewModel.likeActivity(item.usuariCreador, item.dataHoraIni).observe(
                    (context as AppCompatActivity)
                ) {
                    if (it is Result.Success) {
                        Glide.with(holder.mView.context)
                            .load(R.drawable.ic_baseline_favorite_24).centerCrop()
                            .into(holder.iconLikeImage)
                        likedList[position] = true
                    } else {
                        Toast.makeText(context, context.getString(R.string.couldnt_like_activity), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                //del de donar dislike a una activitat
                activitiesViewModel.dislikeActivity(item.usuariCreador, item.dataHoraIni).observe(
                    (context as AppCompatActivity)
                ) {
                    if (it is Result.Success) {
                        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_border_24).centerCrop().into(holder.iconLikeImage)
                        likedList[position] = false
                    } else {
                        Toast.makeText(context, context.getString(R.string.couldnt_dislike_activity), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }*/
    }


/*    mRecyclerViewAdapter.registerAdapterDataObserver(myObserver);


    protected fun setupRecyclerView() {
        mAdapter = MyAdapter(listActivitiesFull)
        mAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkAdapterIsEmpty()
            }
        })
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setAdapter(mAdapter)
        checkAdapterIsEmpty()
    }*/

    /**
     * gets the number of views
     * @return the number of views
     */
    override fun getItemCount(): Int = activitiesList.size

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param activitiesList is the new list of activites to set
     */
    fun setData(activitiesList: List<ActivityFromList>?, likedList: ArrayList<Boolean>?) {
        this.activitiesList = activitiesList!!
        this.likedList = likedList!!
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
        val textViewCapacity: TextView = mView.findViewById(R.id.textViewCapacity)
        val dataTimeImage: ImageView = mView.findViewById(R.id.imageViewDataTime)
        val capacityImage: ImageView = mView.findViewById(R.id.imageViewCapacity)
        val iconLikeImage: ImageView = mView.findViewById(R.id.imageViewIconLike)
        val background: CardView = mView.findViewById(R.id.cardViewBackground)

        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }

    fun performFiltering(constraint: CharSequence?) {
        if (tempListAct.isNotEmpty()) activitiesList = tempListAct

        tempListAct = ArrayList(activitiesList)
        this.listActivitiesFull = ArrayList(activitiesList)

        val charSearch = constraint.toString()
        listActivitiesFull = if (charSearch.isEmpty()) tempListAct
        else {
            val resultList = ArrayList<ActivityFromList>()
            for (row in activitiesList) {
                if (row.titol.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) resultList.add(
                    row
                )
            }
            resultList
        }
        activitiesList = listActivitiesFull
        notifyDataSetChanged()
    }
}
