package com.offhome.app.ui.activitieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList

class  ActivitiesListRecyclerViewAdapter : RecyclerView.Adapter<ActivitiesListRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as ActivityFromList
    }
    private var activitiesList: List<ActivityFromList> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = activitiesList[position]
        holder.textViewName.text = item.name
        holder.textViewDataTime.text = item.dataTime
        holder.textViewCapacity.text = item.capacity
        Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_24).centerCrop().into(holder.imageViewIconLike)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
        var clicked = false

        holder.imageViewIconLike.setOnClickListener {
            clicked = !clicked
            if (clicked) {
                Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_24).centerCrop().into(holder.imageViewIconLike)
            } else {
                Glide.with(holder.mView.context).load(R.drawable.ic_baseline_favorite_border_24).centerCrop().into(holder.imageViewIconLike)
            }
        }
    }

    override fun getItemCount(): Int = activitiesList.size

    fun setData(activitiesList: List<ActivityFromList>?) {
        this.activitiesList = activitiesList!!
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewName: TextView = mView.findViewById(R.id.textViewNameActivity)
        val textViewDataTime: TextView = mView.findViewById(R.id.textViewDataTimeActivity)
        val textViewCapacity: TextView = mView.findViewById(R.id.textViewCapacity)
        val imageViewIconLike: ImageView = mView.findViewById(R.id.imageViewIconLike)

        override fun toString(): String {
            return super.toString()
        }
    }
}
