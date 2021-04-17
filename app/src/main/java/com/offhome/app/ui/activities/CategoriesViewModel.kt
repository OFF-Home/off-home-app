package com.offhome.app.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.CategoryRepository
import com.offhome.app.model.Category


/**
 * ViewModel for Categories
 * @property repository is the repository to get the categories
 * @property categories is the list of categories as live data
 */
class CategoriesViewModel : ViewModel() {
    private var repository: CategoryRepository = CategoryRepository()
    private var categories: LiveData<List<Category>> = repository.getAll()
   // private var listOfData: List<String> = getCategories()
   // private var searchResults: LiveData<List<String>?> = MutableLiveData<Any?>(listOfData)


    /**
     * gets the categories from the repository
     */
    fun getCategories(): LiveData<List<Category>> {
        return categories
    }

    fun filterData(query: String?){
        //getCategories().value = getFilter().filter(query);
    }

    fun getSearchResults() {
       // return searchResults
    }
/*
    fun getNewCategories(): LiveData<List<Category>> {
        categories = repository.getAll()
        return categories
    }
 */
}

