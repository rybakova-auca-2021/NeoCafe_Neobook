package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.ProductDetail
import com.example.neocafe.model.PromotionDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetProductDetailViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getProductDetail(
        id: Int,
        onSuccess: (ProductDetail) -> Unit
    ) {
        apiInterface.getProductDetail(id)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (ProductDetail) -> Unit): Callback<ProductDetail> {
        return object : Callback<ProductDetail> {
            override fun onResponse(call: Call<ProductDetail>, response: Response<ProductDetail>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductDetail>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}