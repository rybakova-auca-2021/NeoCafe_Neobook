package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteNotification : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun deleteNotifications(
        id: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val token = Utils.access
        val authHeader = "Bearer $token"
        apiInterface.deleteNotification(id)
            .enqueue(getCallback(onSuccess, onError))
    }

    private fun getCallback(onSuccess: () -> Unit, onError: () -> Unit): Callback<Unit> {
        return object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke()
                    }
                } else {
                    onError.invoke()
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onError.invoke()
                println("Request failed: ${t.message}")
            }
        }
    }
}