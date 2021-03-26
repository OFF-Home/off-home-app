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

    /**
     * gets the categories from the repository
     */
    fun getCategories(): LiveData<List<Category>> {
        return categories
    }
/*
    fun getNewCategories(): LiveData<List<Category>> {
        categories = repository.getAll()
        return categories
    }
 */
}

