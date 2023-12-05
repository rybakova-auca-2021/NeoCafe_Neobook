package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.Bonus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BonusesViewModel : ViewModel() {
    fun getBonusesAmount(
        onSuccess: (Bonus) -> Unit,
    ) {
        val apiInterface = RetrofitInstance.authApi
        val token = Utils.access
        val authHeader = "Bearer $token"

        val call = apiInterface.userBonusCard(authHeader)
        call.enqueue(object : Callback<Bonus> {
            override fun onResponse(
                call: Call<Bonus>,
                response: Response<Bonus>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess.invoke(responseBody)
                    }

                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Bonus>, t: Throwable) {
                println("Request failed: ${t.message}")                }
        })
    }
}