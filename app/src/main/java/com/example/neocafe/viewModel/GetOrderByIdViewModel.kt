package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.DetailOrder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetOrderByIdViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getOrder(
        id: Int,
        onSuccess: (DetailOrder) -> Unit
    ) {
        val token = Utils.access
        val authHeader = "Bearer $token"
        apiInterface.getOrderById(authHeader, id)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (DetailOrder) -> Unit): Callback<DetailOrder> {
        return object : Callback<DetailOrder> {
            override fun onResponse(call: Call<DetailOrder>, response: Response<DetailOrder>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DetailOrder>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}