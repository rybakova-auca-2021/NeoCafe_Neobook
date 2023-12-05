package com.example.neocafe.model

data class UserProfile(
    val first_name: String,
    val birth_date: String,
    val email: String,
    val phone: String
)

data class UserProfileUpdate(
    val birth_date: String,
    val email: String,
    val first_name: String,
    val phone: String
)

data class Bonus(
    val user: Int,
    val amount: String
)
