package com.offhome.app.ui.activitieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
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

        //recibir nombre categoria seleccionada
        val arguments = intent.extras
        val titleActivity = arguments?.getString("amount")
        title = titleActivity

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        activitiesListViewModel =
                ViewModelProvider(this).get(ActivitiesListViewModel::class.java)

        activitiesListViewModel = ViewModelProvider(this).get(ActivitiesListViewModel::class.java)
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter()

        layout.layoutManager = LinearLayoutManager(applicationContext)
        layout.adapter = activitiesListAdapter


        activitiesListViewModel.getActivitiesList().observe(
                this,
                Observer {
                    activitiesList = it
                    activitiesListAdapter.setData(activitiesList)
                }
        )
    }
}