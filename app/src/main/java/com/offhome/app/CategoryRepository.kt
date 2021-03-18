package com.offhome.app

import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.retrofit.CategoriesClient
import com.offhome.app.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepository {

    var categories: MutableLiveData<List<Category>>? = null
    private val categoriesClient = CategoriesClient()
    val categoriesService = categoriesClient.getCategoriesService()

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
            }
        })
        return categories as MutableLiveData<List<Category>>
    }
}
