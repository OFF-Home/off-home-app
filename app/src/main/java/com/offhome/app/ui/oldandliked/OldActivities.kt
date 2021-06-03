package com.offhome.app.ui.oldandliked



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.ui.activitieslist.ActivitiesViewModel
import java.util.*

/**
 * Class that defines the fragment to show the List of Old Activities
 * @author Emma Pereira
 * @property oldActivitiesViewModel references the viewmodel of the old activities
 * @property oldActivitiesListAdapter references the adapter for the RecyclerView of the old activities
 * @property oldActivitiesList references the list of old activities that will be displayed on the screen
 */
class OldActivities : AppCompatActivity() {

    private lateinit var oldActivitiesViewModel: ActivitiesViewModel
    private lateinit var oldActivitiesListAdapter: OldActivitiesListRecyclerViewAdapter
    private var oldActivitiesList: MutableList<ActivityFromList> = ArrayList()

    /**
     * This is executed when the activity is launched for the first time or created again.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_activities)

        oldActivitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        oldActivitiesListAdapter = OldActivitiesListRecyclerViewAdapter(this@OldActivities)

        val layout = findViewById<RecyclerView>(R.id.listActivities)
        layout.layoutManager = LinearLayoutManager(applicationContext)
        layout.adapter = oldActivitiesListAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        oldActivitiesViewModel.getOldActivitiesList(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()).observe(
            this,
            Observer {
                if (it is Result.Success) {
                    oldActivitiesList = it.data as MutableList<ActivityFromList>
                    oldActivitiesListAdapter.setData(oldActivitiesList)
                }
            }
        )
    }
}
