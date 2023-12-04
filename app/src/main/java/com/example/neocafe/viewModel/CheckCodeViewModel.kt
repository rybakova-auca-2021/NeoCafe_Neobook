package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.SendCode
import com.example.neocafe.model.SendCodeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckCodeViewModel : ViewModel() {
    fun checkCode(
        code: String,
        userId: String,
        onSuccess: () -> Unit,
        onError:() -> Unit
    ) {
        val request = SendCode(code)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.sendCode(request, userId)
        call.enqueue(object : Callback<SendCodeResponse> {
            override fun onResponse(
                call: Call<SendCodeResponse>,
                response: Response<SendCodeResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Utils.access = responseBody.access
                    }
                    if (responseBody != null) {
                        Utils.refresh = responseBody.refresh
                    }
                } else {
                    onError.invoke()
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SendCodeResponse>, t: Throwable) {
                println("Request failed: ${t.message}")                }
        })
    }
}