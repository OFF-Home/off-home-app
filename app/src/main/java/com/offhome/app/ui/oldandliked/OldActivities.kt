package com.offhome.app.ui.oldandliked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.ui.activitieslist.Activities
import com.offhome.app.ui.activitieslist.ActivitiesListRecyclerViewAdapter
import com.offhome.app.ui.activitieslist.ActivitiesViewModel

class OldActivities : AppCompatActivity() {

    private lateinit var oldActivitiesViewModel: ActivitiesViewModel
    private lateinit var oldActivitiesListAdapter: OldActivitiesListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_activities)

        oldActivitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        oldActivitiesListAdapter = OldActivitiesListRecyclerViewAdapter(context as Activities)

        val layout = findViewById<RecyclerView>(R.id.listActivities)
        layout.layoutManager = LinearLayoutManager(context)
        layout.adapter = oldActivitiesListAdapter

        setHasOptionsMenu(true)




    }
}
