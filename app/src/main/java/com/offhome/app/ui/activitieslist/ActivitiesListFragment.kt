package com.offhome.app.ui.activitieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList

class ActivitiesListFragment : Fragment() {

    companion object {
        fun newInstance() =
            ActivitiesListFragment()
    }

    private lateinit var activitiesViewModel: ActivitiesViewModel
    private lateinit var activitiesListAdapter: ActivitiesListRecyclerViewAdapter
    private var activitiesList: List<ActivityFromList> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activities_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter(context as Activities)

        val layout = view.findViewById<RecyclerView>(R.id.listActivities)
        layout.layoutManager = LinearLayoutManager(context)
        layout.adapter = activitiesListAdapter

        activitiesViewModel.getActivitiesList((activity as Activities).categoryName).observe(
            viewLifecycleOwner,
            Observer {
                activitiesList = it
                activitiesListAdapter.setData(activitiesList)
            }
        )
    }
}
