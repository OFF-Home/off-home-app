package com.offhome.app.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.CategoryRepository
import com.offhome.app.model.Category

class CategoriesViewModel : ViewModel() {
    private var repository: CategoryRepository = CategoryRepository()
    private var categories: LiveData<List<Category>> = repository.getAll()!!

    fun getCategories(): LiveData<List<Category>> {
        return categories
    }

    fun getNewCategories(): LiveData<List<Category>> {
        categories = repository.getAll()
        return categories
    }
}
