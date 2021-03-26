package com.offhome.app.ui.activities

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.offhome.app.R
import com.offhome.app.model.Category
import com.offhome.app.ui.activitieslist.ActivitiesList

/**
 * Adpter for the recycler view of the categories list
 * @param context is the context of the activity
 * @property categories is the list of categories
 */
class MyCategoriesRecyclerViewAdapter(private val context: Context?) : RecyclerView.Adapter<MyCategoriesRecyclerViewAdapter.ViewHolder>() {
    /**
     * Onclick to item. Updated when activitiesList developed
     */
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Category
        val intent = Intent(context, ActivitiesList::class.java)
        intent.putExtra("category", item.categoria)
        if (context != null) {
            context.startActivity(intent)
        }
    }
    private var categories: List<Category> = ArrayList()

    /**
     * it inflates the view of each categori and seves the ViewHolder of the view
     * @param parent is the parent viewGroup
     * @param viewType is the type of the View
     * @return the view holder of the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category, parent, false)
        return ViewHolder(view)
    }

    /**
     * It sets the data to each view
     * @param holder is the view holder of that view
     * @param position is the position of the view to render
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]
        holder.textViewName.text = item.categoria
        if (context != null) {
            Glide.with(context).load(R.drawable.sport).centerCrop().into(holder.imageViewBackground)
            Glide.with(context).load(R.drawable.ic_running_solid).centerCrop().into(holder.imageViewIcon)
        }
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    /**
     * gets the number of views
     * @return the number of views
     */
    override fun getItemCount(): Int = categories.size

    /**
     * sets the new data and notifies to the adapter to refresh if necessary
     * @param categories is the new list of categories to set
     */
    fun setData(categories: List<Category>?) {
        this.categories = categories!!
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class to save the refereces to the views of each view
     * @param mView is the general view
     * @property textViewName is the textView where will render the category name
     * @property imageViewBackground is the image to show behind the category
     * @property imageViewIcon is the icon of the category
     */
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewName: TextView = mView.findViewById(R.id.textViewNameCategory)
        val imageViewBackground: ImageView = mView.findViewById(R.id.pinkBackground)
        val imageViewIcon: ImageView = mView.findViewById(R.id.imageViewIconCategory)

        /**
         * General function that returns the string
         */
        override fun toString(): String {
            return super.toString()
        }
    }
}
