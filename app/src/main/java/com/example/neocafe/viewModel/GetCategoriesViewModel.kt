package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.ProductCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetCategoriesViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getAllCategories(
        onSuccess: (List<ProductCategory>) -> Unit
    ) {
        apiInterface.getProductCategory()
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (List<ProductCategory>) -> Unit): Callback<List<ProductCategory>> {
        return object : Callback<List<ProductCategory>> {
            override fun onResponse(call: Call<List<ProductCategory>>, response: Response<List<ProductCategory>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ProductCategory>>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}