package com.example.neocafe.login

data class LoginRequest(
    val phone: String
)

data class LoginResponse(
    val message: String,
    val user_id: Int
)