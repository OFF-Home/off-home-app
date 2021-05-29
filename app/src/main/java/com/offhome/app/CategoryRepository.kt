package com.offhome.app



import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.model.Category
import com.offhome.app.data.retrofit.CategoriesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Respository class for the Categories
 * @property categories references the liveData of the list of categories
 * @property categoriesClient is the client of the retrofit class to connect with backend
 * @property categoriesService is the service in which are indicated the calls to backend
 */
class CategoryRepository {

    var categories: MutableLiveData<List<Category>>? = null
    private val categoriesClient = CategoriesClient()
    private val categoriesService = categoriesClient.getCategoriesService()

    /**
     * It gets all the categories
     * @return the mutable livedata list of categories
     */
    fun getAll(): MutableLiveData<List<Category>> {
        if (categories == null) categories = MutableLiveData<List<Category>>()
        val call: Call<List<Category>> = categoriesService!!.getAllCategories()
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    categories!!.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                // Error en la connexion
                Log.d("GET", "Error getting info")
            }
        })
        return categories as MutableLiveData<List<Category>>
    }
}
