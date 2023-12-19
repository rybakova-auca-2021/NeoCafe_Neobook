package com.example.neocafe.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Branch(
    val title: String,
    val address: String,
    val start_time: String,
    val end_time: String,
    val status: String,
    val latitude: String,
    val longitude: String
) : Parcelable
