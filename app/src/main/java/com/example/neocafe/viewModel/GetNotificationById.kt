package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.NotificationDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetNotificationById : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getNotification(
        id: Int,
        onSuccess: (NotificationDetail) -> Unit
    ) {
        val token = Utils.access
        val authHeader = "Bearer $token"
        apiInterface.getNotificationById(id)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (NotificationDetail) -> Unit): Callback<NotificationDetail> {
        return object : Callback<NotificationDetail> {
            override fun onResponse(call: Call<NotificationDetail>, response: Response<NotificationDetail>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NotificationDetail>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}