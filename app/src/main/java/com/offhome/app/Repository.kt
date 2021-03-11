package com.offhome.app

import androidx.lifecycle.MutableLiveData
import com.offhome.app.model.Category

class Repository {

    var categories: MutableLiveData<List<Category>>? = null

    fun getAll(): MutableLiveData<List<Category>> {
        val category = Category("Sport", "url", "url")
        if (categories == null) {
            categories = MutableLiveData<List<Category>>(listOf(category, category, category, category, category, category, category, category, category, category, category, category, category, category, category))
        }
        return categories as MutableLiveData<List<Category>>
    }
}
