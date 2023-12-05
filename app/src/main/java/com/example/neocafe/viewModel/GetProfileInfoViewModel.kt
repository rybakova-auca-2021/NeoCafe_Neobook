package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.UserProfile
import com.example.neocafe.model.UserProfileUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetProfileInfoViewModel : ViewModel() {
    fun getProfile(
        onSuccess: (UserProfile) -> Unit,
    ) {
        val apiInterface = RetrofitInstance.authApi

        val token = Utils.access
        val authHeader = "Bearer $token"

        val call = apiInterface.userInfo(authHeader)
        call.enqueue(object : Callback<UserProfile> {
            override fun onResponse(
                call: Call<UserProfile>,
                response: Response<UserProfile>
            ) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    if (profile != null) {
                        onSuccess.invoke(profile)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                println("Request failed: ${t.message}")                }
        })
    }

    fun updateProfile(
        name: String,
        date: String,
        email: String,
        phone: String,
        onSuccess: () -> Unit
    ) {
        val apiInterface = RetrofitInstance.authApi

        val request = UserProfileUpdate(date, email, name, phone)
        val token = Utils.access
        val authHeader = "Bearer $token"

        val call = apiInterface.userProfileUpdate(authHeader, request)
        call.enqueue(object : Callback<UserProfile> {
            override fun onResponse(
                call: Call<UserProfile>,
                response: Response<UserProfile>
            ) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    onSuccess.invoke()
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                println("Request failed: ${t.message}")                }
        })
    }
}