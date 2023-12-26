package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.Notification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetNotificationsViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getAllNotifications(
        onSuccess: (List<Notification>) -> Unit
    ) {
        apiInterface.getNotification()
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (List<Notification>) -> Unit): Callback<List<Notification>> {
        return object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}