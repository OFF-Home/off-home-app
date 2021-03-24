package com.offhome.app.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.model.Category

class CategoriesViewModel : ViewModel() {
    private var repository: Repository = Repository()
    private var categories: LiveData<List<Category>> = repository.getAll()!!

    fun getCategories(): LiveData<List<Category>> {
        return categories
    }
}
