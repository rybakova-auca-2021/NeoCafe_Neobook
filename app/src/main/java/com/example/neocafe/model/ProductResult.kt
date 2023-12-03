package com.example.neocafe.model

data class ProductResult(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Product>
)
