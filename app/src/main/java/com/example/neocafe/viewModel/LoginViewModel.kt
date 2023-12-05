package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.Login
import com.example.neocafe.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    fun login(
        phone: String,
        onSuccess: () -> Unit,
        onError:() -> Unit
    ) {
        val request = Login(phone)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.login(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        println("UserIdResponse: ${responseBody.user_id}")
                        Utils.userId = responseBody.user_id
                        println("UserIdModel: ${Utils.userId}")
                    }

                } else {
                    onError.invoke()
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println("Request failed: ${t.message}")                }
        })
    }
}