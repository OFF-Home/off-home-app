package com.offhome.app.ui.activitieslist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList
import java.util.*
import kotlin.collections.ArrayList


/**
 * Class that defines the fragment to show the List of Activities
 * @author Emma Pereira
 * @property activitiesViewModel references the viewmodel of the activities
 * @property activitiesListAdapter references the adapter for the RecyclerView of the activities
 * @property activitiesList references the list of activities that will be displayed on the screen
 */
class ActivitiesListFragment : Fragment() {

    companion object {
        fun newInstance() =
            ActivitiesListFragment()
    }

    private lateinit var activitiesViewModel: ActivitiesViewModel
    private lateinit var activitiesListAdapter: ActivitiesListRecyclerViewAdapter
    private var activitiesList: List<ActivityFromList> = ArrayList()
    private val spinnerDialog = view?.findViewById<Spinner>(R.id.spinnerCategories)

    /**
     * Called to initialize the fragment and has the observers, returns the view inflated
     * @param inflater is the Layout inflater to inflate the view
     * @param container is the part which contains the view
     * @param savedInstanceState is the last saved instance of the view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activities_list_fragment, container, false)
    }

    /**
     * Called once the view is inflated and here is where we display the information and we initizalize other views
     * @param view is the view initialized by the onCreateView function
     * @param savedInstanceState is the last saved instance of the view
     */
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

    /**
     * Function that creates the options menu and it is called when the user opens the menu for the first time to search or filter the activities from the list
     * @param menu is the menu provided in the callback
     * @param inflater it inflates the menu and adds items
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_and_sort_buttons, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        /**
         * Callbacks for changes to the query text
         */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /**
             * Called when the user submits the query
             * @param query the query text that is to be submitted
             * @return true if the query has been handled by the listener, false to let the SearchView perform the default action.
             * */
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            /**
             * Called when the query text is changed by the user
             * @param query the new content of the query text field
             * @return false if the SearchView should perform the default action of showing any suggestions if available, true if the action was handled by the listener.
             */
            override fun onQueryTextChange(newText: String?): Boolean {
                activitiesListAdapter.performFiltering(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sort) {
            sortActivities()
        } else if (id == R.id.action_sort_categories) {
            sortActivitiesByCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortActivities() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("                    Sort")
            .setItems(arrayOf("Ascending", "Descending", "By date")) { dialogInterface, i ->
                when (i) {
                    0 -> {
                        //ascending clicked
                        dialogInterface.dismiss()
                        activitiesList.sortedBy { it.titol }
                    }
                    1 -> {
                        //descending clicked
                        dialogInterface.dismiss()
                        activitiesList.sortedByDescending { it.titol }
                    }
                    2 -> {
                        //sorted by date
                        dialogInterface.dismiss()
                        activitiesList.sortedBy { it.dataHoraIni }
                    }
                }
            }.show()
    }

    private fun sortActivitiesByCategory() {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        val selectedList = ArrayList<Int>()
        val categoriesOptions: Array<String> = resources.getStringArray(R.array.select_category)
        builder.setTitle("Sort by category")
        val arrayChecked = booleanArrayOf(false, false, false, false, false, false)
        builder.setMultiChoiceItems(
            R.array.select_category, null
        ) { dialog, which, isChecked ->
            // Update the clicked item checked status
            arrayChecked[which] = isChecked
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains((which))) {
                selectedList.remove(Integer.valueOf(which))
            }
        }
        builder.setPositiveButton("OK") { _, _ ->
            // Do something when click positive button
            //for (i in categoriesOptions.indices) {
             //   val checked = arrayChecked[i]
              //  if (checked) {}
            // Display the clicked Ok button
            Toast.makeText(context, "Filter applied", Toast.LENGTH_SHORT).show()
        }
        dialog = builder.create()
        dialog.show()
    }

}
