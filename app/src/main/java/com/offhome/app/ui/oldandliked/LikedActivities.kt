package com.offhome.app.ui.oldandliked



import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.ui.activitieslist.ActivitiesViewModel
import java.util.ArrayList

/**
 * Class that defines the fragment to show the List of Liked Activities
 * @author Emma Pereira
 * @property likedActivitiesViewModel references the viewmodel of the liked activities
 * @property likedActivitiesListAdapter references the adapter for the RecyclerView of the liked activities
 * @property likedActivitiesList references the list of liked activities that will be displayed on the screen
 */
class LikedActivities : AppCompatActivity() {

    private lateinit var likedActivitiesViewModel: ActivitiesViewModel
    private lateinit var likedActivitiesListAdapter: LikedActivitiesListRecyclerViewAdapter
    private var likedActivitiesList: MutableList<ActivityFromList> = ArrayList()

    /**
     * This is executed when the activity is launched for the first time or created again.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_activities)

        likedActivitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        likedActivitiesListAdapter = LikedActivitiesListRecyclerViewAdapter(this@LikedActivities)


        val layout = findViewById<RecyclerView>(R.id.listActivities)
        layout.layoutManager = LinearLayoutManager(applicationContext)
        layout.adapter = likedActivitiesListAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        likedActivitiesViewModel.getLikedActivitiesList(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()).observe(
            this, Observer {
                if (it is Result.Success) {
                    likedActivitiesList = it.data as MutableList<ActivityFromList>
                    likedActivitiesListAdapter.setData(likedActivitiesList)
                }
            }
        )

    }
}
