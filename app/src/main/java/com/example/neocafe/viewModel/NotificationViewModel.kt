package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.NotificationRequest
import com.example.neocafe.model.Promotion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationViewModel : ViewModel() {

    fun sendNotification(
        title: String,
        body: String,
        tokens: String,
    ) {
        val apiInterface = RetrofitInstance.mainApi

        val request = NotificationRequest(tokens, title, body)

        val call = apiInterface.sendNotification(request)
        call.enqueue(object : Callback<NotificationRequest> {
            override fun onResponse(
                call: Call<NotificationRequest>,
                response: Response<NotificationRequest>
            ) {
                if (response.isSuccessful) {
                    println("Notification was sent successfully")
                } else {
                    println("Failed to send notification. HTTP error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NotificationRequest>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        })
    }
}
