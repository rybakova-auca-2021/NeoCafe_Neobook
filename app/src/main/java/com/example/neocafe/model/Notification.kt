package com.example.neocafe.model

data class Notification(
    val id: Int,
    val title: String,
    val body: String,
    val created_date: String
)

data class NotificationDetail(
    val id: Int,
    val title: String,
    val body: String,
)

data class NotificationRequest(
    val registration_tokens: String,
    val title: String,
    val body: String
)


