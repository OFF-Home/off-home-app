package com.offhome.app.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.Category

class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var categoryAdapter: MyCategoriesRecyclerViewAdapter
    private var categories: List<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoriesViewModel =
            ViewModelProvider(this).get(CategoriesViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        categoriesViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        categoryAdapter = MyCategoriesRecyclerViewAdapter()

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
}
