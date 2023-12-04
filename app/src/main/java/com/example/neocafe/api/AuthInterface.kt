package com.example.neocafe.api

import com.example.neocafe.model.Bonus
import com.example.neocafe.model.Login
import com.example.neocafe.model.RegisterUser
import com.example.neocafe.model.RegisterUserResponse
import com.example.neocafe.model.RegisterWithPhone
import com.example.neocafe.model.SendCode
import com.example.neocafe.model.SendCodeResponse
import com.example.neocafe.model.UserProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthInterface {
    @POST("users/register/")
    fun registerUser(@Body request: RegisterUser) : Call<RegisterUserResponse>

    @POST("users/register-with-phone/")
    fun registerUserWithPhone(@Body request: RegisterWithPhone) : Call<RegisterWithPhone>

    @POST("users/send-code/{user_id}/")
    fun sendCode(@Body request: SendCode, @Path("user_id") userId: String) : Call<SendCodeResponse>

    @POST("users/login/")
    fun login(@Body request: Login) : Call<Login>

    @GET("users/me/")
    fun userInfo(@Body request: UserProfile) : Call<UserProfile>

    @GET("users/bonus-card/")
    fun userBonusCard(@Body request: Bonus) : Call<Bonus>

    @PUT("users/profile/update/")
    fun userProfileUpdate(@Body request: UserProfile) : Call<UserProfile>
}