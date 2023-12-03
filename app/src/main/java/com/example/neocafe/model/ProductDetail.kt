package com.example.neocafe.model

data class ProductDetail(
    val id: Int,
    val title: String,
    val description: String,
    val category: Int,
    val image: String?,
    val quantity: Int,
    val price: String
)
