package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.GetOrder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetOrdersViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getMyOrders(
        onSuccess: (List<GetOrder>) -> Unit
    ) {
        val token = Utils.access
        val authHeader = "Bearer $token"
        apiInterface.getOrders(authHeader)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (List<GetOrder>) -> Unit): Callback<List<GetOrder>> {
        return object : Callback<List<GetOrder>> {
            override fun onResponse(call: Call<List<GetOrder>>, response: Response<List<GetOrder>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<GetOrder>>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}