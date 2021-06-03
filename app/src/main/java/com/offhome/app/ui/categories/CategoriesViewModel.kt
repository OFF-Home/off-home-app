package com.offhome.app.ui.categories



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.CategoryRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.Category

/**
 * ViewModel for Categories
 * @property repository is the repository to get the categories
 * @property categories is the list of categories as live data
 */
class CategoriesViewModel : ViewModel() {
    private var repository: CategoryRepository = CategoryRepository()
    private var categories: MutableLiveData<Result<List<Category>>> = repository.getAll()

    /**
     * gets the categories from the repository
     */
    fun getCategories(): MutableLiveData<Result<List<Category>>> {
        return categories
    }
}
