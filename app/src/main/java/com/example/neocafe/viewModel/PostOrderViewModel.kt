package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.GetOrder
import com.example.neocafe.model.Order
import com.example.neocafe.model.OrderConfirm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostOrderViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun postOrder(
        order: Order,
        onSuccess: (OrderConfirm) -> Unit
    ) {
        val token = Utils.access
        val authHeader = "Bearer $token"

        apiInterface.postOrders(authHeader, order)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (OrderConfirm) -> Unit): Callback<OrderConfirm> {
        return object : Callback<OrderConfirm> {
            override fun onResponse(call: Call<OrderConfirm>, response: Response<OrderConfirm>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<OrderConfirm>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}