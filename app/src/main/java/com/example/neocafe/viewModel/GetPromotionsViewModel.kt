package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.Promotion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPromotionsViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getAllPromotions(
        onSuccess: (List<Promotion>) -> Unit
    ) {
        apiInterface.getPromotions()
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (List<Promotion>) -> Unit): Callback<List<Promotion>> {
        return object : Callback<List<Promotion>> {
            override fun onResponse(call: Call<List<Promotion>>, response: Response<List<Promotion>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Promotion>>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}