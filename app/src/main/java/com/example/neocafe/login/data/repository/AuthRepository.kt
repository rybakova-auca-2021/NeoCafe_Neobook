package com.example.neocafe.login.data.repository

import com.example.neocafe.login.LoginRequest
import com.example.neocafe.login.LoginResponse
import com.example.neocafe.login.data.api.AuthApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val authApi: AuthApi) {
	
	fun requestLogin(
		loginRequest: LoginRequest,
		onSuccess: (LoginResponse) -> Unit,
		onFailure: (Throwable) -> Unit,
	) = authApi.login(loginRequest).enqueue(object : Callback<LoginResponse> {
		override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
			response.body()?.let { onSuccess.invoke(it) }
		}
		
		override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
			onFailure.invoke(t)
		}
		
	})
	
}