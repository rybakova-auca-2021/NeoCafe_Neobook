package com.example.neocafe.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neocafe.login.LoginRequest
import com.example.neocafe.login.data.repository.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
	
	private val _result = MutableLiveData<Result<Boolean>>()
	val result: LiveData<Result<Boolean>>
		get() = _result
	
	fun login(
		phone: String,
	) {
		val request = LoginRequest(phone)
		authRepository.requestLogin(loginRequest = request,
			onSuccess = {
				_result.value = Result.success(true)
			},
			onFailure = {
				_result.value = Result.failure(it)
			})
	}
}