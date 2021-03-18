package com.offhome.app.ui.createactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.Repository
import com.offhome.app.model.Category

class CreateActivityViewModel : ViewModel() {
    private var repository: Repository = Repository()
    private var categories: LiveData<List<Category>> = repository.getAll()!!

    private val _text = MutableLiveData<String>().apply {
        value = "This is CreationActivity Activity"
    }
    val text: LiveData<String> = _text

    fun getCategories(): LiveData<List<Category>> {
        return categories
    }

    fun checkedCorrectCategory(){
        getCategories()
    }
}