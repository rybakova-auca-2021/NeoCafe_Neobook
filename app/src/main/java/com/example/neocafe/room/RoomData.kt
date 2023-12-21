package com.example.neocafe.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity (tableName = "ProductDB")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val category: Int,
    val image: String?,
    val quantity: Int,
    val price: String,
    var isInCart: Boolean = false
) : Parcelable
