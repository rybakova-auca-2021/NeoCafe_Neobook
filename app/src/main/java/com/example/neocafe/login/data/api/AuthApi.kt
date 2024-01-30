package com.example.neocafe.login.data.api

import com.example.neocafe.login.LoginRequest
import com.example.neocafe.login.LoginResponse
import com.example.neocafe.model.Bonus
import com.example.neocafe.model.RegisterUser
import com.example.neocafe.model.RegisterUserResponse
import com.example.neocafe.model.RegisterWithPhone
import com.example.neocafe.model.SendCode
import com.example.neocafe.model.SendCodeResponse
import com.example.neocafe.model.UserProfile
import com.example.neocafe.model.UserProfileUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
//    @Multipart
//    @POST("users/register/")
//    fun registerUser(
//        @Part("first_name") name: RequestBody,
//        @Part("phone") phone: RequestBody,
//        @Part qr_code: MultipartBody.Part?,
//    ) : Call<RegisterUserResponse>

    @POST("users/register/")
    fun registerUser(
        @Body request: RegisterUser
    ) : Call<RegisterUserResponse>

    @POST("users/register-with-phone/")
    fun registerUserWithPhone(@Body request: RegisterWithPhone) : Call<RegisterWithPhone>

    @POST("users/send-code/{user_id}/")
    fun sendCode(@Body request: SendCode, @Path("user_id") userId: String) : Call<SendCodeResponse>

    @POST("users/login/")
    fun login(@Body request: LoginRequest) : Call<LoginResponse>

    @GET("users/me/")
    fun userInfo(@Header("Authorization") token: String) : Call<UserProfile>

    @GET("users/bonus-card/")
    fun userBonusCard(@Header("Authorization") token: String) : Call<Bonus>

    @PUT("users/profile/update/")
    fun userProfileUpdate(@Header("Authorization") token: String, @Body request: UserProfileUpdate) : Call<UserProfile>
}