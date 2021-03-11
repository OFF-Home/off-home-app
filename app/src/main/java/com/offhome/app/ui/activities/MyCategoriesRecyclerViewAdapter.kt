package com.offhome.app.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.offhome.app.R
import com.offhome.app.model.Category

class MyCategoriesRecyclerViewAdapter : RecyclerView.Adapter<MyCategoriesRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Category
    }
    private var categories: List<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]
        holder.textViewName.text = item.name
        Glide.with(holder.mView.context).load(R.drawable.sport).centerCrop().into(holder.imageViewBackground)
        Glide.with(holder.mView.context).load(R.drawable.ic_running_solid).centerCrop().into(holder.imageViewIcon)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun setData(categories: List<Category>?) {
        this.categories = categories!!
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewName: TextView = mView.findViewById(R.id.textViewNameCategory)
        val imageViewBackground: ImageView = mView.findViewById(R.id.imageViewBackground)
        val imageViewIcon: ImageView = mView.findViewById(R.id.imageViewIconCategory)

        override fun toString(): String {
            return super.toString()
        }
    }
}
