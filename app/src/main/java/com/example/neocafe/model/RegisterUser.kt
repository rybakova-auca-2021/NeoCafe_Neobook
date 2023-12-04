package com.example.neocafe.model

data class RegisterUser(
    val first_name: String,
    val phone: String
)

data class RegisterUserResponse(
    val message: String,
    val user: User
)

data class User(
    val id: Int,
    val first_name: String,
    val phone: String
)


data class RegisterWithPhone(
    val phone: String
)

data class SendCode(
    val code: String
)

data class SendCodeResponse(
    val message: String,
    val refresh: String,
    val access: String
)