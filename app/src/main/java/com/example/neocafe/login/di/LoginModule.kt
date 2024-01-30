package com.example.neocafe.login.di

import com.example.neocafe.constants.Constant
import com.example.neocafe.login.data.api.AuthApi
import com.example.neocafe.login.data.repository.AuthRepository
import com.example.neocafe.login.presentation.LoginViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//module
//scope

val loginModule = module {
	
	///todo: move okhttp builder and retrofit to other module ,  called network module
	single {
		val client = OkHttpClient.Builder()
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.build()
		Retrofit.Builder()
			.baseUrl(Constant.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.build().create(AuthApi::class.java)
	}
	
	single {
		AuthRepository(get()) // receive dependency)
	}
	
	viewModel {
		LoginViewModel(get())
	}
	
}