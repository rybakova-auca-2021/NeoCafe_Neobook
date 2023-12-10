package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPopularProductsViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getAllPopularProducts(
        onSuccess: (List<Product>) -> Unit
    ) {
        apiInterface.getPopularProducts()
            .enqueue(getCallback(onSuccess))
    }

    fun getProductsBySearch(
        search: String,
        onSuccess: (List<Product>) -> Unit
    ) {
        apiInterface.getProducts(search = search)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (List<Product>) -> Unit): Callback<List<Product>> {
        return object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}