package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.PromotionDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPromotionDetail : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getPromotionDetail(
        id: Int,
        onSuccess: (PromotionDetail) -> Unit
    ) {
        apiInterface.getPromotionDetail(id)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (PromotionDetail) -> Unit): Callback<PromotionDetail> {
        return object : Callback<PromotionDetail> {
            override fun onResponse(call: Call<PromotionDetail>, response: Response<PromotionDetail>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PromotionDetail>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}