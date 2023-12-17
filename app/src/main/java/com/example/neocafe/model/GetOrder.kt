package com.example.neocafe.model

data class Order(
    val products: List<OrderItem>,
    val user: Int,
    val delivery_type: String,
    val address: String,
    val apartment: String,
    val intercom_code: String,
    val entrance: String,
    val floor: String,
    val phone: String,
    val change_from: Int,
    val comment: String,
    val pickup_branch: Int,
    val cutlery: Int,
    val qr_code: String,
    val use_bonus: String,
    val coupon_code: String
)

data class GetOrder(
    val order_number: Int,
    val total_amount: String,
    val created_date: String,
    val user: Int,
    val status: String,
    val order_item: List<OrderItem>
)

data class DetailOrder(
    val order_number: Int,
    val order_item: List<OrderItem>,
    val status: String,
    val created_date: String,
    val products_amount: String,
    val spent_bonus: String,
    val user: Int,
    val delivery_type: String,
    val address: String,
    val pickup_branch: Int,
    val cutlery: Int,
    val total_amount: String,
    val promo_code: String
)

data class OrderConfirm(
    val order_number: Int,
    val total_amount: String,
    val created_date: String,
    val user: Int,
    val delivery_type: String,
    val address: String,
    val apartment: String,
    val intercom_code: String,
    val entrance: String,
    val floor: String,
    val phone: String,
    val change_from: Int,
    val comment: String,
    val pickup_branch: Int,
    val cutlery: Int,
    val qr_code: String,
    val use_bonus: String,
)

data class OrderItem(
    val product: Int,
    val quantity: Int
)
