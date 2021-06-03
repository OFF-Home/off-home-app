package com.offhome.app.ui.activitieslist



import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList
import java.text.ParseException
import java.text.SimpleDateFormat
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
    private var activitiesList: MutableList<ActivityFromList> = ArrayList()
    private var likedList = ArrayList<Boolean>()
    private var likedActivitiesList: MutableList<ActivityFromList>? = ArrayList()
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
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter(context as Activities, activitiesViewModel)

        val layout = view.findViewById<RecyclerView>(R.id.listActivities)
        layout.layoutManager = LinearLayoutManager(context)
        layout.adapter = activitiesListAdapter

        setHasOptionsMenu(true)

        // get the current date
        val currentTime = Calendar.getInstance().time

        //obtener actividades a las que el usuario ha dado like
        activitiesViewModel.getLikedActivitiesList(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()).observe(
            viewLifecycleOwner,
            Observer {
                if (it is Result.Success) {
                    likedActivitiesList = it.data as MutableList<ActivityFromList>
                    activitiesViewModel.getActivitiesList((activity as Activities).categoryName).observe(
                        viewLifecycleOwner,
                        Observer {
                            if (it is Result.Success) {
                                for (item in it.data) {
                                    // transform dataHoraIni into date format
                                    val mydate = item.dataHoraFi
                                    var date: Date? = null
                                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                                    try {
                                        date = format.parse(mydate)
                                    } catch (e: ParseException) {
                                        e.printStackTrace()
                                    }

                                    if (date != null) {
                                        if (date > currentTime) {
                                            activitiesList.add(item)
                                        }
                                    }
                                }
                                likedList.clear()
                                for (item in activitiesList) {
                                    val found = likedActivitiesList?.find { element -> element == item }
                                    if (found == item) likedList.add(true)
                                    else likedList.add(false)
                                }
                                activitiesListAdapter.setData(activitiesList, likedList)
                            }

                        }
                    )
                }
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

    /**
     * This function handles the option selected by the user in the fragment's toolbar. The options are: sort the list of activities by some parameters or filter by category
     * @param item: The options menu in which you place your items
     * @return true if the menu is to be displayed; false if it will not be shown
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sort) {
            sortActivities()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * This Function handles the dialog where the activities can be sorted. The opcions are: by ascending, by descending and by date
     */
    private fun sortActivities() {
        val currentTime = Calendar.getInstance().time
        val builder = AlertDialog.Builder(context)
        builder.setTitle("                    Sort")
            .setItems(arrayOf("Ascending", "Descending", "By date")) { dialogInterface, i ->
                when (i) {
                    0 -> {
                        // ascending clicked
                        dialogInterface.dismiss()
                        activitiesViewModel.getActivitiesByAscTitle().observe(
                            viewLifecycleOwner,
                            Observer {
                                if (it is Result.Success) {
                                    activitiesList = ArrayList()
                                    for (item in it.data) {
                                        // transform dataHoraIni into date format
                                        val mydate = item.dataHoraFi
                                        var date: Date? = null
                                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                                        try {
                                            date = format.parse(mydate)
                                        } catch (e: ParseException) {
                                            e.printStackTrace()
                                        }

                                        if (date != null) {
                                            if (date > currentTime) {
                                                activitiesList.add(item)
                                            }
                                        }
                                    }
                                    likedList.clear()
                                    for (item in activitiesList) {
                                        val found = likedActivitiesList?.find { element -> element == item }
                                        if (found == item) likedList.add(true)
                                        else likedList.add(false)
                                    }
                                    activitiesListAdapter.setData(activitiesList, likedList)
                                }
                                else if (it is Result.Error) Log.d("SORT", "Error getting the acitvities sorted by ascendant order")
                            }
                        )
                    }
                    1 -> {
                        // descending clicked
                        dialogInterface.dismiss()
                        activitiesViewModel.getActivitiesByDescTitle().observe(
                            viewLifecycleOwner,
                            Observer {
                                if (it is Result.Success) {
                                    activitiesList = ArrayList()
                                    for (item in it.data) {
                                        // transform dataHoraIni into date format
                                        val mydate = item.dataHoraFi
                                        var date: Date? = null
                                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                                        try {
                                            date = format.parse(mydate)
                                        } catch (e: ParseException) {
                                            e.printStackTrace()
                                        }

                                        if (date != null) {
                                            if (date!! > currentTime) {
                                                activitiesList.add(item)
                                            }
                                        }
                                    }
                                    likedList.clear()
                                    for (item in activitiesList) {
                                        val found = likedActivitiesList?.find { element -> element == item }
                                        if (found == item) likedList.add(true)
                                        else likedList.add(false)
                                    }
                                    activitiesListAdapter.setData(activitiesList, likedList)
                                }
                                else if (it is Result.Error) Log.d("SORT", "Error getting the acitvities sorted by descendant order")
                            }
                        )
                    }
                    2 -> {
                        // sorted by date
                        dialogInterface.dismiss()
                        activitiesViewModel.getActivitiesByDate().observe(
                            viewLifecycleOwner,
                            Observer {
                                if (it is Result.Success) {
                                    activitiesList = ArrayList()
                                    for (item in it.data) {
                                        // transform dataHoraIni into date format
                                        val mydate = item.dataHoraFi
                                        var date: Date? = null
                                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                                        try {
                                            date = format.parse(mydate)
                                        } catch (e: ParseException) {
                                            e.printStackTrace()
                                        }

                                        if (date != null) {
                                            if (date!! > currentTime) {
                                                activitiesList.add(item)
                                            }
                                        }
                                    }
                                    likedList.clear()
                                    for (item in activitiesList) {
                                        val found = likedActivitiesList?.find { element -> element == item }
                                        if (found == item) likedList.add(true)
                                        else likedList.add(false)
                                    }
                                    activitiesListAdapter.setData(activitiesList, likedList)
                                }
                                else if (it is Result.Error) Log.d("SORT", "Error getting the activities sorted by descendant order")
                            }
                        )
                    }
                }
            }.show()
    }

    /**
     * This function handles the dialog that appears to filter the activities by the category or categories selected by the multi choice option
     */
    private fun sortActivitiesByCategory() {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        val selectedList = ArrayList<Int>()
        val categoriesOptions: Array<String> = resources.getStringArray(R.array.select_category)
        builder.setTitle("Sort by category")
        val arrayChecked = booleanArrayOf(false, false, false, false, false, false, false, false)
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
            val resultlist = ArrayList<ActivityFromList>()
            for (j in selectedList.indices) {
                for (s in activitiesList) {
                    if (s.categoria == (categoriesOptions)[selectedList[j]])
                        resultlist.add(s)
                }
            }
            // activitiesList = resultlist
            Toast.makeText(context, "Filter applied", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}
