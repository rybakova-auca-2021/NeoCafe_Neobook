package com.example.neocafe.model

data class Product(
    val id: Int,
    val title: String,
    val category: Int,
    val image: String?,
    val quantity: Int,
    val price: String
)
