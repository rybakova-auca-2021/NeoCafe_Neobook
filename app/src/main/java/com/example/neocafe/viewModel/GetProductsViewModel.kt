package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetProductsViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi

    // Function with only search parameter
    fun getProductsBySearch(
        search: String,
        onSuccess: (List<Product>) -> Unit
    ) {
        apiInterface.getProducts(search = search)
            .enqueue(getCallback(onSuccess))
    }

    // Function with only category parameter
    fun getProductsByCategory(
        category: String,
        onSuccess: (List<Product>) -> Unit
    ) {
        apiInterface.getProducts(category = category)
            .enqueue(getCallback(onSuccess))
    }

    // Function with only ordering parameter
    fun getProductsByOrdering(
        ordering: String,
        onSuccess: (List<Product>) -> Unit
    ) {
        apiInterface.getProducts(ordering = ordering)
            .enqueue(getCallback(onSuccess))
    }

    fun getProductsByLimit(
        limit: Int,
        onSuccess: (List<Product>) -> Unit
    ) {
        apiInterface.getProducts(limit = limit)
            .enqueue(getCallback(onSuccess))
    }

    // Function without search or category parameters
    fun getAllProducts(
        onSuccess: (List<Product>) -> Unit
    ) {
        apiInterface.getProducts()
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
