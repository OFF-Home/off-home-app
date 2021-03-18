package com.offhome.app.ui.activitieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList

class ActivitiesList : AppCompatActivity() {

    /*
    private lateinit var activitiesListViewModel: ActivitiesListViewModel
    private lateinit var activitiesListAdapter: ActivitiesListRecyclerViewAdapter
    private var activitiesList: List<ActivityFromList> = ArrayList()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities_list)

        val imgLike = findViewById<ImageView>(R.id.imageViewIconLike)
        var clicked = false

        imgLike.setOnClickListener {
            clicked = !clicked
            if (clicked) {
                imgLike.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            else {
                imgLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }
        //recibir nombre categoria seleccionada
        val arguments = intent.extras
        val titleActivity = arguments?.getString("amount")
        title = titleActivity
    }

    /*
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        activitiesListViewModel =
                ViewModelProvider(this).get(ActivitiesListViewModel::class.java)
        val view = inflater.inflate(R.layout.activity_activities_list, container, false)

        activitiesListViewModel = ViewModelProvider(this).get(ActivitiesListViewModel::class.java)
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter()

        if (view is RecyclerView) {
            with(view) {
                layoutManager = GridLayoutManager(context, 2)
                adapter = activitiesListAdapter
            }
        }

        activitiesListViewModel.getActivitiesList().observe(
                viewLifecycleOwner,
                Observer {
                    activitiesList = it
                    activitiesListAdapter.setData(activitiesList)
                }
        )

        return view
    }*/
}