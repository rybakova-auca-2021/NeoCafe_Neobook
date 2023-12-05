package com.example.neocafe.model

data class Login(
    val phone: String
)

data class LoginResponse(
    val message: String,
    val user_id: Int
)