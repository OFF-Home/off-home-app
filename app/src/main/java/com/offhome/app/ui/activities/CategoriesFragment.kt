package com.offhome.app.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.Category

/**
 * Class that defines the fragment to show the Categories
 * @property categoriesViewModel references the viewmodel of this fragment
 * @property categoryAdapter is the adapter for the RecyclerView of the cateories
 * @property categories is the list of categories
 */
class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var categoryAdapter: MyCategoriesRecyclerViewAdapter
    private var categories: List<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Called when view created and has the observers
     * @param inflater is the Layout inflater to inflate the view
     * @param container is the part which contains the view
     * @param savedInstanceState is the last saved instance of the view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoriesViewModel =
            ViewModelProvider(this).get(CategoriesViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        categoryAdapter = MyCategoriesRecyclerViewAdapter(context)

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = GridLayoutManager(context, 2)
                adapter = categoryAdapter
            }
        }

        categoriesViewModel.getCategories().observe(
            viewLifecycleOwner,
            {
                categories = it
                categoryAdapter.setData(categories)
            }
        )

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_button, menu)
        super.onCreateOptionsMenu(menu, inflater)

        //aixo es per fer la cerca però encara no funciona IN PROCESS
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                categoriesViewModel.filterData(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

     /*   categoriesViewModel.getSearchResults().observe(viewLifecycleOwner, { searchResults->
            Log.w("onActivityCreated()", "new search list received :" +searchResults)
            categoryAdapter.setData(searchResults)
        });*/
    }
}

