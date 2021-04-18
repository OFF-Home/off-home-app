package com.offhome.app.ui.activitieslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList

class ActivitiesList : AppCompatActivity() {

    private lateinit var activitiesListViewModel: ActivitiesListViewModel
    private lateinit var activitiesListAdapter: ActivitiesListRecyclerViewAdapter
    private var activitiesList: List<ActivityFromList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities_list)

        val layout = findViewById<RecyclerView>(R.id.listActivities)

        // recibir nombre categoria seleccionada
        val arguments = intent.extras
        val categoryName = arguments?.getString("category")
        title = categoryName

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activitiesListViewModel =
            ViewModelProvider(this).get(ActivitiesListViewModel::class.java)

        activitiesListViewModel = ViewModelProvider(this).get(ActivitiesListViewModel::class.java)
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter(applicationContext)

        layout.layoutManager = LinearLayoutManager(applicationContext)
        layout.adapter = activitiesListAdapter

        activitiesListViewModel.getActivitiesList(categoryName!!).observe(
            this,
            Observer {
                activitiesList = it
                activitiesListAdapter.setData(activitiesList)
            }
        )
    }
}
