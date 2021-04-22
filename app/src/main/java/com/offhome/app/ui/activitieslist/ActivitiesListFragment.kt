package com.offhome.app.ui.activitieslist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
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

        setHasOptionsMenu(true)

        activitiesViewModel.getActivitiesList((activity as Activities).categoryName).observe(
            viewLifecycleOwner,
            Observer {
                activitiesList = it
                activitiesListAdapter.setData(activitiesList)
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                activitiesListAdapter.performFiltering(newText)
                return false
            }
        })
    }

    //per fer el sort
            //sort ascending/descending , order by data, seleccionar quina categoria es vol
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      /*  when (item.itemId) {
            R.id.activity_title -> sortActivities()
        }*/
        val id = item.itemId
        if (id == R.id.action_sort){
            sortActivities()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortActivities() {

        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Sort").setItems(arrayOf("Ascending", "Descending", "By date", "By category")){
                dialogInterface, i ->
            if (i==0){
                //ascending clicked
                dialogInterface.dismiss()
            }
            else {
                dialogInterface.dismiss()
                //descending clicked
            }
        }.show()
    }

}
