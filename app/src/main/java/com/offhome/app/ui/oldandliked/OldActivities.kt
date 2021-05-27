package com.offhome.app.ui.oldandliked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.ActivityFromList
import com.offhome.app.ui.activitieslist.ActivitiesViewModel
import java.util.*

class OldActivities : AppCompatActivity() {

    private lateinit var oldActivitiesViewModel: ActivitiesViewModel
    private lateinit var oldActivitiesListAdapter: OldActivitiesListRecyclerViewAdapter
    private var oldActivitiesList: MutableList<ActivityFromList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_activities)

        oldActivitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        oldActivitiesListAdapter = OldActivitiesListRecyclerViewAdapter(applicationContext)

        val layout = findViewById<RecyclerView>(R.id.listActivities)
        layout.layoutManager = LinearLayoutManager(applicationContext)
        layout.adapter = oldActivitiesListAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        oldActivitiesViewModel.getOldActivitiesList(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()).observe(
            this, Observer {
                oldActivitiesList = it as MutableList<ActivityFromList>
                /*for (item in oldActivitiesList) {
                    if (item.valoracio == null) item.valoracio = 0
                }*/
                oldActivitiesListAdapter.setData(oldActivitiesList)
            }
        )
    }
}
