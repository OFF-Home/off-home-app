package com.offhome.app.ui.oldandliked



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R
import com.offhome.app.ui.activitieslist.ActivitiesListRecyclerViewAdapter
import com.offhome.app.ui.activitieslist.ActivitiesViewModel

class LikedActivities : AppCompatActivity() {

    private lateinit var likedActivitiesViewModel: ActivitiesViewModel
    private lateinit var likedActivitiesListAdapter: ActivitiesListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_activities)

        likedActivitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        /*likedActivitiesListAdapter = ActivitiesListRecyclerViewAdapter(context as Activities)

        val layout = findViewById<RecyclerView>(R.id.listActivities)
        layout.layoutManager = LinearLayoutManager(context)
        layout.adapter = likedActivitiesListAdapter

        setHasOptionsMenu(true)*/
    }
}
