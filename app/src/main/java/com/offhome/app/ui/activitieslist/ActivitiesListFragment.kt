package com.offhome.app.ui.activitieslist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
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

    private val spinnerDialog = view?.findViewById<Spinner>(R.id.spinnerCategories)


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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        if (id == R.id.action_sort) {
            sortActivities()
        }
        else if (id == R.id.action_sort_categories){
            sortActivitiesByCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortActivities() {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Sort")
            .setItems(arrayOf("Ascending", "Descending", "By date")) { dialogInterface, i ->
                if (i == 0) {
                    //ascending clicked
                    dialogInterface.dismiss()
                    activitiesList.sortedBy { it.titol }
                } else if (i == 1) {
                    //descending clicked
                    dialogInterface.dismiss()
                    activitiesList.sortedByDescending { it.titol }
                } else if (i == 2) {
                    //sorted by date
                    dialogInterface.dismiss()
                    activitiesList.sortedBy { it.dataHoraIni }
                }
            }.show()
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivitiesListFragment.this,android.R.layout.spinnerDialog);

    }

    private fun sortActivitiesByCategory(){
        val dialog2 = AlertDialog.Builder(context)
        dialog2.setTitle("Sort by category").setItems(spinnerDialog?.resources?.getStringArray(R.array.select_category)) { dialogInterface, i ->
            dialogInterface.dismiss()
        }.show()

       /* val adapter = ArrayAdapter.createFromResource(this, R.array.select_category, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDialog.setAdapter(adapter)
        spinnerDialog.setOnItemSelectedListener()*/
    }
}
       /* dialog.setItems("select_category", DialogInterface.OnClickListener { dialogInterface, item -> // Do something with the selection
            dialogInterface.dismiss()
            Toast.makeText((ActivitiesListFragment.this,select_aa))
            })    }*/

